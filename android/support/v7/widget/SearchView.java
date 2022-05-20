package android.support.v7.widget;

import android.annotation.TargetApi;
import android.app.PendingIntent;
import android.app.SearchableInfo;
import android.content.ActivityNotFoundException;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.database.Cursor;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.ResultReceiver;
import android.support.annotation.Nullable;
import android.support.annotation.RestrictTo;
import android.support.v4.content.res.ConfigurationHelper;
import android.support.v4.os.ParcelableCompat;
import android.support.v4.os.ParcelableCompatCreatorCallbacks;
import android.support.v4.view.AbsSavedState;
import android.support.v4.view.KeyEventCompat;
import android.support.v4.view.ViewCompat;
import android.support.v4.widget.CursorAdapter;
import android.support.v7.appcompat.R;
import android.support.v7.view.CollapsibleActionView;
import android.text.Editable;
import android.text.SpannableStringBuilder;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.text.style.ImageSpan;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.TouchDelegate;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewTreeObserver;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.TextView;
import java.lang.reflect.Method;
import java.util.WeakHashMap;

public class SearchView extends LinearLayoutCompat implements CollapsibleActionView {
  static final boolean DBG = false;
  
  static final AutoCompleteTextViewReflector HIDDEN_METHOD_INVOKER = new AutoCompleteTextViewReflector();
  
  private static final String IME_OPTION_NO_MICROPHONE = "nm";
  
  static final String LOG_TAG = "SearchView";
  
  private Bundle mAppSearchData;
  
  private boolean mClearingFocus;
  
  final ImageView mCloseButton;
  
  private final ImageView mCollapsedIcon;
  
  private int mCollapsedImeOptions;
  
  private final CharSequence mDefaultQueryHint;
  
  private final View mDropDownAnchor;
  
  private boolean mExpandedInActionView;
  
  final ImageView mGoButton;
  
  private boolean mIconified;
  
  private boolean mIconifiedByDefault;
  
  private int mMaxWidth;
  
  private CharSequence mOldQueryText;
  
  private final View.OnClickListener mOnClickListener = new View.OnClickListener() {
      public void onClick(View param1View) {
        if (param1View == SearchView.this.mSearchButton) {
          SearchView.this.onSearchClicked();
          return;
        } 
        if (param1View == SearchView.this.mCloseButton) {
          SearchView.this.onCloseClicked();
          return;
        } 
        if (param1View == SearchView.this.mGoButton) {
          SearchView.this.onSubmitQuery();
          return;
        } 
        if (param1View == SearchView.this.mVoiceButton) {
          SearchView.this.onVoiceClicked();
          return;
        } 
        if (param1View == SearchView.this.mSearchSrcTextView)
          SearchView.this.forceSuggestionQuery(); 
      }
    };
  
  private OnCloseListener mOnCloseListener;
  
  private final TextView.OnEditorActionListener mOnEditorActionListener = new TextView.OnEditorActionListener() {
      public boolean onEditorAction(TextView param1TextView, int param1Int, KeyEvent param1KeyEvent) {
        SearchView.this.onSubmitQuery();
        return true;
      }
    };
  
  private final AdapterView.OnItemClickListener mOnItemClickListener = new AdapterView.OnItemClickListener() {
      public void onItemClick(AdapterView<?> param1AdapterView, View param1View, int param1Int, long param1Long) {
        SearchView.this.onItemClicked(param1Int, 0, (String)null);
      }
    };
  
  private final AdapterView.OnItemSelectedListener mOnItemSelectedListener = new AdapterView.OnItemSelectedListener() {
      public void onItemSelected(AdapterView<?> param1AdapterView, View param1View, int param1Int, long param1Long) {
        SearchView.this.onItemSelected(param1Int);
      }
      
      public void onNothingSelected(AdapterView<?> param1AdapterView) {}
    };
  
  private OnQueryTextListener mOnQueryChangeListener;
  
  View.OnFocusChangeListener mOnQueryTextFocusChangeListener;
  
  private View.OnClickListener mOnSearchClickListener;
  
  private OnSuggestionListener mOnSuggestionListener;
  
  private final WeakHashMap<String, Drawable.ConstantState> mOutsideDrawablesCache = new WeakHashMap<String, Drawable.ConstantState>();
  
  private CharSequence mQueryHint;
  
  private boolean mQueryRefinement;
  
  private Runnable mReleaseCursorRunnable = new Runnable() {
      public void run() {
        if (SearchView.this.mSuggestionsAdapter != null && SearchView.this.mSuggestionsAdapter instanceof SuggestionsAdapter)
          SearchView.this.mSuggestionsAdapter.changeCursor(null); 
      }
    };
  
  final ImageView mSearchButton;
  
  private final View mSearchEditFrame;
  
  private final Drawable mSearchHintIcon;
  
  private final View mSearchPlate;
  
  final SearchAutoComplete mSearchSrcTextView;
  
  private Rect mSearchSrcTextViewBounds = new Rect();
  
  private Rect mSearchSrtTextViewBoundsExpanded = new Rect();
  
  SearchableInfo mSearchable;
  
  private Runnable mShowImeRunnable = new Runnable() {
      public void run() {
        InputMethodManager inputMethodManager = (InputMethodManager)SearchView.this.getContext().getSystemService("input_method");
        if (inputMethodManager != null)
          SearchView.HIDDEN_METHOD_INVOKER.showSoftInputUnchecked(inputMethodManager, (View)SearchView.this, 0); 
      }
    };
  
  private final View mSubmitArea;
  
  private boolean mSubmitButtonEnabled;
  
  private final int mSuggestionCommitIconResId;
  
  private final int mSuggestionRowLayout;
  
  CursorAdapter mSuggestionsAdapter;
  
  private int[] mTemp = new int[2];
  
  private int[] mTemp2 = new int[2];
  
  View.OnKeyListener mTextKeyListener = new View.OnKeyListener() {
      public boolean onKey(View param1View, int param1Int, KeyEvent param1KeyEvent) {
        boolean bool1 = false;
        if (SearchView.this.mSearchable == null)
          return bool1; 
        if (SearchView.this.mSearchSrcTextView.isPopupShowing() && SearchView.this.mSearchSrcTextView.getListSelection() != -1)
          return SearchView.this.onSuggestionsKey(param1View, param1Int, param1KeyEvent); 
        boolean bool2 = bool1;
        if (!SearchView.this.mSearchSrcTextView.isEmpty()) {
          bool2 = bool1;
          if (KeyEventCompat.hasNoModifiers(param1KeyEvent)) {
            bool2 = bool1;
            if (param1KeyEvent.getAction() == 1) {
              bool2 = bool1;
              if (param1Int == 66) {
                param1View.cancelLongPress();
                SearchView.this.launchQuerySearch(0, (String)null, SearchView.this.mSearchSrcTextView.getText().toString());
                bool2 = true;
              } 
            } 
          } 
        } 
        return bool2;
      }
    };
  
  private TextWatcher mTextWatcher = new TextWatcher() {
      public void afterTextChanged(Editable param1Editable) {}
      
      public void beforeTextChanged(CharSequence param1CharSequence, int param1Int1, int param1Int2, int param1Int3) {}
      
      public void onTextChanged(CharSequence param1CharSequence, int param1Int1, int param1Int2, int param1Int3) {
        SearchView.this.onTextChanged(param1CharSequence);
      }
    };
  
  private UpdatableTouchDelegate mTouchDelegate;
  
  private final Runnable mUpdateDrawableStateRunnable = new Runnable() {
      public void run() {
        SearchView.this.updateFocusedState();
      }
    };
  
  private CharSequence mUserQuery;
  
  private final Intent mVoiceAppSearchIntent;
  
  final ImageView mVoiceButton;
  
  private boolean mVoiceButtonEnabled;
  
  private final Intent mVoiceWebSearchIntent;
  
  public SearchView(Context paramContext) {
    this(paramContext, (AttributeSet)null);
  }
  
  public SearchView(Context paramContext, AttributeSet paramAttributeSet) {
    this(paramContext, paramAttributeSet, R.attr.searchViewStyle);
  }
  
  public SearchView(Context paramContext, AttributeSet paramAttributeSet, int paramInt) {
    super(paramContext, paramAttributeSet, paramInt);
    TintTypedArray tintTypedArray = TintTypedArray.obtainStyledAttributes(paramContext, paramAttributeSet, R.styleable.SearchView, paramInt, 0);
    LayoutInflater.from(paramContext).inflate(tintTypedArray.getResourceId(R.styleable.SearchView_layout, R.layout.abc_search_view), this, true);
    this.mSearchSrcTextView = (SearchAutoComplete)findViewById(R.id.search_src_text);
    this.mSearchSrcTextView.setSearchView(this);
    this.mSearchEditFrame = findViewById(R.id.search_edit_frame);
    this.mSearchPlate = findViewById(R.id.search_plate);
    this.mSubmitArea = findViewById(R.id.submit_area);
    this.mSearchButton = (ImageView)findViewById(R.id.search_button);
    this.mGoButton = (ImageView)findViewById(R.id.search_go_btn);
    this.mCloseButton = (ImageView)findViewById(R.id.search_close_btn);
    this.mVoiceButton = (ImageView)findViewById(R.id.search_voice_btn);
    this.mCollapsedIcon = (ImageView)findViewById(R.id.search_mag_icon);
    ViewCompat.setBackground(this.mSearchPlate, tintTypedArray.getDrawable(R.styleable.SearchView_queryBackground));
    ViewCompat.setBackground(this.mSubmitArea, tintTypedArray.getDrawable(R.styleable.SearchView_submitBackground));
    this.mSearchButton.setImageDrawable(tintTypedArray.getDrawable(R.styleable.SearchView_searchIcon));
    this.mGoButton.setImageDrawable(tintTypedArray.getDrawable(R.styleable.SearchView_goIcon));
    this.mCloseButton.setImageDrawable(tintTypedArray.getDrawable(R.styleable.SearchView_closeIcon));
    this.mVoiceButton.setImageDrawable(tintTypedArray.getDrawable(R.styleable.SearchView_voiceIcon));
    this.mCollapsedIcon.setImageDrawable(tintTypedArray.getDrawable(R.styleable.SearchView_searchIcon));
    this.mSearchHintIcon = tintTypedArray.getDrawable(R.styleable.SearchView_searchHintIcon);
    this.mSuggestionRowLayout = tintTypedArray.getResourceId(R.styleable.SearchView_suggestionRowLayout, R.layout.abc_search_dropdown_item_icons_2line);
    this.mSuggestionCommitIconResId = tintTypedArray.getResourceId(R.styleable.SearchView_commitIcon, 0);
    this.mSearchButton.setOnClickListener(this.mOnClickListener);
    this.mCloseButton.setOnClickListener(this.mOnClickListener);
    this.mGoButton.setOnClickListener(this.mOnClickListener);
    this.mVoiceButton.setOnClickListener(this.mOnClickListener);
    this.mSearchSrcTextView.setOnClickListener(this.mOnClickListener);
    this.mSearchSrcTextView.addTextChangedListener(this.mTextWatcher);
    this.mSearchSrcTextView.setOnEditorActionListener(this.mOnEditorActionListener);
    this.mSearchSrcTextView.setOnItemClickListener(this.mOnItemClickListener);
    this.mSearchSrcTextView.setOnItemSelectedListener(this.mOnItemSelectedListener);
    this.mSearchSrcTextView.setOnKeyListener(this.mTextKeyListener);
    this.mSearchSrcTextView.setOnFocusChangeListener(new View.OnFocusChangeListener() {
          public void onFocusChange(View param1View, boolean param1Boolean) {
            if (SearchView.this.mOnQueryTextFocusChangeListener != null)
              SearchView.this.mOnQueryTextFocusChangeListener.onFocusChange((View)SearchView.this, param1Boolean); 
          }
        });
    setIconifiedByDefault(tintTypedArray.getBoolean(R.styleable.SearchView_iconifiedByDefault, true));
    paramInt = tintTypedArray.getDimensionPixelSize(R.styleable.SearchView_android_maxWidth, -1);
    if (paramInt != -1)
      setMaxWidth(paramInt); 
    this.mDefaultQueryHint = tintTypedArray.getText(R.styleable.SearchView_defaultQueryHint);
    this.mQueryHint = tintTypedArray.getText(R.styleable.SearchView_queryHint);
    paramInt = tintTypedArray.getInt(R.styleable.SearchView_android_imeOptions, -1);
    if (paramInt != -1)
      setImeOptions(paramInt); 
    paramInt = tintTypedArray.getInt(R.styleable.SearchView_android_inputType, -1);
    if (paramInt != -1)
      setInputType(paramInt); 
    setFocusable(tintTypedArray.getBoolean(R.styleable.SearchView_android_focusable, true));
    tintTypedArray.recycle();
    this.mVoiceWebSearchIntent = new Intent("android.speech.action.WEB_SEARCH");
    this.mVoiceWebSearchIntent.addFlags(268435456);
    this.mVoiceWebSearchIntent.putExtra("android.speech.extra.LANGUAGE_MODEL", "web_search");
    this.mVoiceAppSearchIntent = new Intent("android.speech.action.RECOGNIZE_SPEECH");
    this.mVoiceAppSearchIntent.addFlags(268435456);
    this.mDropDownAnchor = findViewById(this.mSearchSrcTextView.getDropDownAnchor());
    if (this.mDropDownAnchor != null)
      if (Build.VERSION.SDK_INT >= 11) {
        addOnLayoutChangeListenerToDropDownAnchorSDK11();
      } else {
        addOnLayoutChangeListenerToDropDownAnchorBase();
      }  
    updateViewsVisibility(this.mIconifiedByDefault);
    updateQueryHint();
  }
  
  private void addOnLayoutChangeListenerToDropDownAnchorBase() {
    this.mDropDownAnchor.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
          public void onGlobalLayout() {
            SearchView.this.adjustDropDownSizeAndPosition();
          }
        });
  }
  
  @TargetApi(11)
  private void addOnLayoutChangeListenerToDropDownAnchorSDK11() {
    this.mDropDownAnchor.addOnLayoutChangeListener(new View.OnLayoutChangeListener() {
          public void onLayoutChange(View param1View, int param1Int1, int param1Int2, int param1Int3, int param1Int4, int param1Int5, int param1Int6, int param1Int7, int param1Int8) {
            SearchView.this.adjustDropDownSizeAndPosition();
          }
        });
  }
  
  private Intent createIntent(String paramString1, Uri paramUri, String paramString2, String paramString3, int paramInt, String paramString4) {
    Intent intent = new Intent(paramString1);
    intent.addFlags(268435456);
    if (paramUri != null)
      intent.setData(paramUri); 
    intent.putExtra("user_query", this.mUserQuery);
    if (paramString3 != null)
      intent.putExtra("query", paramString3); 
    if (paramString2 != null)
      intent.putExtra("intent_extra_data_key", paramString2); 
    if (this.mAppSearchData != null)
      intent.putExtra("app_data", this.mAppSearchData); 
    if (paramInt != 0) {
      intent.putExtra("action_key", paramInt);
      intent.putExtra("action_msg", paramString4);
    } 
    intent.setComponent(this.mSearchable.getSearchActivity());
    return intent;
  }
  
  private Intent createIntentFromSuggestion(Cursor paramCursor, int paramInt, String paramString) {
    Intent intent;
    try {
      Uri uri;
      String str1 = SuggestionsAdapter.getColumnString(paramCursor, "suggest_intent_action");
      String str2 = str1;
      if (str1 == null)
        str2 = this.mSearchable.getSuggestIntentAction(); 
      str1 = str2;
      if (str2 == null)
        str1 = "android.intent.action.SEARCH"; 
      String str3 = SuggestionsAdapter.getColumnString(paramCursor, "suggest_intent_data");
      str2 = str3;
      if (str3 == null)
        str2 = this.mSearchable.getSuggestIntentData(); 
      str3 = str2;
      if (str2 != null) {
        String str = SuggestionsAdapter.getColumnString(paramCursor, "suggest_intent_data_id");
        str3 = str2;
        if (str != null) {
          StringBuilder stringBuilder = new StringBuilder();
          this();
          str3 = stringBuilder.append(str2).append("/").append(Uri.encode(str)).toString();
        } 
      } 
      if (str3 == null) {
        str2 = null;
      } else {
        uri = Uri.parse(str3);
      } 
      str3 = SuggestionsAdapter.getColumnString(paramCursor, "suggest_intent_query");
      Intent intent1 = createIntent(str1, uri, SuggestionsAdapter.getColumnString(paramCursor, "suggest_intent_extra_data"), str3, paramInt, paramString);
      intent = intent1;
    } catch (RuntimeException runtimeException) {}
    return intent;
  }
  
  private Intent createVoiceAppSearchIntent(Intent paramIntent, SearchableInfo paramSearchableInfo) {
    String str2;
    ComponentName componentName = paramSearchableInfo.getSearchActivity();
    Intent intent1 = new Intent("android.intent.action.SEARCH");
    intent1.setComponent(componentName);
    PendingIntent pendingIntent = PendingIntent.getActivity(getContext(), 0, intent1, 1073741824);
    Bundle bundle = new Bundle();
    if (this.mAppSearchData != null)
      bundle.putParcelable("app_data", (Parcelable)this.mAppSearchData); 
    Intent intent2 = new Intent(paramIntent);
    String str1 = "free_form";
    intent1 = null;
    String str3 = null;
    int i = 1;
    Resources resources = getResources();
    if (paramSearchableInfo.getVoiceLanguageModeId() != 0)
      str1 = resources.getString(paramSearchableInfo.getVoiceLanguageModeId()); 
    if (paramSearchableInfo.getVoicePromptTextId() != 0)
      str2 = resources.getString(paramSearchableInfo.getVoicePromptTextId()); 
    if (paramSearchableInfo.getVoiceLanguageId() != 0)
      str3 = resources.getString(paramSearchableInfo.getVoiceLanguageId()); 
    if (paramSearchableInfo.getVoiceMaxResults() != 0)
      i = paramSearchableInfo.getVoiceMaxResults(); 
    intent2.putExtra("android.speech.extra.LANGUAGE_MODEL", str1);
    intent2.putExtra("android.speech.extra.PROMPT", str2);
    intent2.putExtra("android.speech.extra.LANGUAGE", str3);
    intent2.putExtra("android.speech.extra.MAX_RESULTS", i);
    if (componentName == null) {
      str1 = null;
      intent2.putExtra("calling_package", str1);
      intent2.putExtra("android.speech.extra.RESULTS_PENDINGINTENT", (Parcelable)pendingIntent);
      intent2.putExtra("android.speech.extra.RESULTS_PENDINGINTENT_BUNDLE", bundle);
      return intent2;
    } 
    str1 = componentName.flattenToShortString();
    intent2.putExtra("calling_package", str1);
    intent2.putExtra("android.speech.extra.RESULTS_PENDINGINTENT", (Parcelable)pendingIntent);
    intent2.putExtra("android.speech.extra.RESULTS_PENDINGINTENT_BUNDLE", bundle);
    return intent2;
  }
  
  private Intent createVoiceWebSearchIntent(Intent paramIntent, SearchableInfo paramSearchableInfo) {
    Intent intent = new Intent(paramIntent);
    ComponentName componentName = paramSearchableInfo.getSearchActivity();
    if (componentName == null) {
      componentName = null;
      intent.putExtra("calling_package", (String)componentName);
      return intent;
    } 
    String str = componentName.flattenToShortString();
    intent.putExtra("calling_package", str);
    return intent;
  }
  
  private void dismissSuggestions() {
    this.mSearchSrcTextView.dismissDropDown();
  }
  
  private void getChildBoundsWithinSearchView(View paramView, Rect paramRect) {
    paramView.getLocationInWindow(this.mTemp);
    getLocationInWindow(this.mTemp2);
    int i = this.mTemp[1] - this.mTemp2[1];
    int j = this.mTemp[0] - this.mTemp2[0];
    paramRect.set(j, i, paramView.getWidth() + j, paramView.getHeight() + i);
  }
  
  private CharSequence getDecoratedHint(CharSequence paramCharSequence) {
    SpannableStringBuilder spannableStringBuilder;
    if (this.mIconifiedByDefault && this.mSearchHintIcon != null) {
      int i = (int)(this.mSearchSrcTextView.getTextSize() * 1.25D);
      this.mSearchHintIcon.setBounds(0, 0, i, i);
      SpannableStringBuilder spannableStringBuilder1 = new SpannableStringBuilder("   ");
      spannableStringBuilder1.setSpan(new ImageSpan(this.mSearchHintIcon), 1, 2, 33);
      spannableStringBuilder1.append(paramCharSequence);
      spannableStringBuilder = spannableStringBuilder1;
    } 
    return (CharSequence)spannableStringBuilder;
  }
  
  private int getPreferredHeight() {
    return getContext().getResources().getDimensionPixelSize(R.dimen.abc_search_view_preferred_height);
  }
  
  private int getPreferredWidth() {
    return getContext().getResources().getDimensionPixelSize(R.dimen.abc_search_view_preferred_width);
  }
  
  private boolean hasVoiceSearch() {
    boolean bool1 = false;
    boolean bool2 = bool1;
    if (this.mSearchable != null) {
      bool2 = bool1;
      if (this.mSearchable.getVoiceSearchEnabled()) {
        Intent intent = null;
        if (this.mSearchable.getVoiceSearchLaunchWebSearch()) {
          intent = this.mVoiceWebSearchIntent;
        } else if (this.mSearchable.getVoiceSearchLaunchRecognizer()) {
          intent = this.mVoiceAppSearchIntent;
        } 
        bool2 = bool1;
        if (intent != null) {
          bool2 = bool1;
          if (getContext().getPackageManager().resolveActivity(intent, 65536) != null)
            bool2 = true; 
        } 
      } 
    } 
    return bool2;
  }
  
  static boolean isLandscapeMode(Context paramContext) {
    return ((paramContext.getResources().getConfiguration()).orientation == 2);
  }
  
  private boolean isSubmitAreaEnabled() {
    return ((this.mSubmitButtonEnabled || this.mVoiceButtonEnabled) && !isIconified());
  }
  
  private void launchIntent(Intent paramIntent) {
    if (paramIntent != null)
      try {
        getContext().startActivity(paramIntent);
      } catch (RuntimeException runtimeException) {
        Log.e("SearchView", "Failed launch activity: " + paramIntent, runtimeException);
      }  
  }
  
  private boolean launchSuggestion(int paramInt1, int paramInt2, String paramString) {
    Cursor cursor = this.mSuggestionsAdapter.getCursor();
    if (cursor != null && cursor.moveToPosition(paramInt1)) {
      launchIntent(createIntentFromSuggestion(cursor, paramInt2, paramString));
      return true;
    } 
    return false;
  }
  
  private void postUpdateFocusedState() {
    post(this.mUpdateDrawableStateRunnable);
  }
  
  private void rewriteQueryFromSuggestion(int paramInt) {
    Editable editable = this.mSearchSrcTextView.getText();
    Cursor cursor = this.mSuggestionsAdapter.getCursor();
    if (cursor != null) {
      if (cursor.moveToPosition(paramInt)) {
        CharSequence charSequence = this.mSuggestionsAdapter.convertToString(cursor);
        if (charSequence != null) {
          setQuery(charSequence);
          return;
        } 
        setQuery((CharSequence)editable);
        return;
      } 
      setQuery((CharSequence)editable);
    } 
  }
  
  private void setQuery(CharSequence paramCharSequence) {
    int i;
    this.mSearchSrcTextView.setText(paramCharSequence);
    SearchAutoComplete searchAutoComplete = this.mSearchSrcTextView;
    if (TextUtils.isEmpty(paramCharSequence)) {
      i = 0;
    } else {
      i = paramCharSequence.length();
    } 
    searchAutoComplete.setSelection(i);
  }
  
  private void updateCloseButton() {
    boolean bool;
    byte b1 = 1;
    byte b2 = 0;
    if (!TextUtils.isEmpty((CharSequence)this.mSearchSrcTextView.getText())) {
      bool = true;
    } else {
      bool = false;
    } 
    byte b3 = b1;
    if (!bool)
      if (this.mIconifiedByDefault && !this.mExpandedInActionView) {
        b3 = b1;
      } else {
        b3 = 0;
      }  
    ImageView imageView = this.mCloseButton;
    if (b3) {
      b3 = b2;
    } else {
      b3 = 8;
    } 
    imageView.setVisibility(b3);
    Drawable drawable = this.mCloseButton.getDrawable();
    if (drawable != null) {
      int[] arrayOfInt;
      if (bool) {
        arrayOfInt = ENABLED_STATE_SET;
      } else {
        arrayOfInt = EMPTY_STATE_SET;
      } 
      drawable.setState(arrayOfInt);
    } 
  }
  
  private void updateQueryHint() {
    CharSequence charSequence1 = getQueryHint();
    SearchAutoComplete searchAutoComplete = this.mSearchSrcTextView;
    CharSequence charSequence2 = charSequence1;
    if (charSequence1 == null)
      charSequence2 = ""; 
    searchAutoComplete.setHint(getDecoratedHint(charSequence2));
  }
  
  private void updateSearchAutoComplete() {
    boolean bool = true;
    this.mSearchSrcTextView.setThreshold(this.mSearchable.getSuggestThreshold());
    this.mSearchSrcTextView.setImeOptions(this.mSearchable.getImeOptions());
    int i = this.mSearchable.getInputType();
    int j = i;
    if ((i & 0xF) == 1) {
      i &= 0xFFFEFFFF;
      j = i;
      if (this.mSearchable.getSuggestAuthority() != null)
        j = i | 0x10000 | 0x80000; 
    } 
    this.mSearchSrcTextView.setInputType(j);
    if (this.mSuggestionsAdapter != null)
      this.mSuggestionsAdapter.changeCursor(null); 
    if (this.mSearchable.getSuggestAuthority() != null) {
      this.mSuggestionsAdapter = (CursorAdapter)new SuggestionsAdapter(getContext(), this, this.mSearchable, this.mOutsideDrawablesCache);
      this.mSearchSrcTextView.setAdapter((ListAdapter)this.mSuggestionsAdapter);
      SuggestionsAdapter suggestionsAdapter = (SuggestionsAdapter)this.mSuggestionsAdapter;
      j = bool;
      if (this.mQueryRefinement)
        j = 2; 
      suggestionsAdapter.setQueryRefinement(j);
    } 
  }
  
  private void updateSubmitArea() {
    // Byte code:
    //   0: bipush #8
    //   2: istore_1
    //   3: iload_1
    //   4: istore_2
    //   5: aload_0
    //   6: invokespecial isSubmitAreaEnabled : ()Z
    //   9: ifeq -> 36
    //   12: aload_0
    //   13: getfield mGoButton : Landroid/widget/ImageView;
    //   16: invokevirtual getVisibility : ()I
    //   19: ifeq -> 34
    //   22: iload_1
    //   23: istore_2
    //   24: aload_0
    //   25: getfield mVoiceButton : Landroid/widget/ImageView;
    //   28: invokevirtual getVisibility : ()I
    //   31: ifne -> 36
    //   34: iconst_0
    //   35: istore_2
    //   36: aload_0
    //   37: getfield mSubmitArea : Landroid/view/View;
    //   40: iload_2
    //   41: invokevirtual setVisibility : (I)V
    //   44: return
  }
  
  private void updateSubmitButton(boolean paramBoolean) {
    // Byte code:
    //   0: bipush #8
    //   2: istore_2
    //   3: iload_2
    //   4: istore_3
    //   5: aload_0
    //   6: getfield mSubmitButtonEnabled : Z
    //   9: ifeq -> 45
    //   12: iload_2
    //   13: istore_3
    //   14: aload_0
    //   15: invokespecial isSubmitAreaEnabled : ()Z
    //   18: ifeq -> 45
    //   21: iload_2
    //   22: istore_3
    //   23: aload_0
    //   24: invokevirtual hasFocus : ()Z
    //   27: ifeq -> 45
    //   30: iload_1
    //   31: ifne -> 43
    //   34: iload_2
    //   35: istore_3
    //   36: aload_0
    //   37: getfield mVoiceButtonEnabled : Z
    //   40: ifne -> 45
    //   43: iconst_0
    //   44: istore_3
    //   45: aload_0
    //   46: getfield mGoButton : Landroid/widget/ImageView;
    //   49: iload_3
    //   50: invokevirtual setVisibility : (I)V
    //   53: return
  }
  
  private void updateViewsVisibility(boolean paramBoolean) {
    byte b2;
    boolean bool2;
    byte b1 = 8;
    boolean bool1 = true;
    this.mIconified = paramBoolean;
    if (paramBoolean) {
      b2 = 0;
    } else {
      b2 = 8;
    } 
    if (!TextUtils.isEmpty((CharSequence)this.mSearchSrcTextView.getText())) {
      bool2 = true;
    } else {
      bool2 = false;
    } 
    this.mSearchButton.setVisibility(b2);
    updateSubmitButton(bool2);
    View view = this.mSearchEditFrame;
    if (paramBoolean) {
      b2 = b1;
    } else {
      b2 = 0;
    } 
    view.setVisibility(b2);
    if (this.mCollapsedIcon.getDrawable() == null || this.mIconifiedByDefault) {
      b2 = 8;
    } else {
      b2 = 0;
    } 
    this.mCollapsedIcon.setVisibility(b2);
    updateCloseButton();
    if (!bool2) {
      paramBoolean = bool1;
    } else {
      paramBoolean = false;
    } 
    updateVoiceButton(paramBoolean);
    updateSubmitArea();
  }
  
  private void updateVoiceButton(boolean paramBoolean) {
    byte b1 = 8;
    byte b2 = b1;
    if (this.mVoiceButtonEnabled) {
      b2 = b1;
      if (!isIconified()) {
        b2 = b1;
        if (paramBoolean) {
          b2 = 0;
          this.mGoButton.setVisibility(8);
        } 
      } 
    } 
    this.mVoiceButton.setVisibility(b2);
  }
  
  void adjustDropDownSizeAndPosition() {
    if (this.mDropDownAnchor.getWidth() > 1) {
      byte b;
      Resources resources = getContext().getResources();
      int i = this.mSearchPlate.getPaddingLeft();
      Rect rect = new Rect();
      boolean bool = ViewUtils.isLayoutRtl((View)this);
      if (this.mIconifiedByDefault) {
        b = resources.getDimensionPixelSize(R.dimen.abc_dropdownitem_icon_width) + resources.getDimensionPixelSize(R.dimen.abc_dropdownitem_text_padding_left);
      } else {
        b = 0;
      } 
      this.mSearchSrcTextView.getDropDownBackground().getPadding(rect);
      if (bool) {
        j = -rect.left;
      } else {
        j = i - rect.left + b;
      } 
      this.mSearchSrcTextView.setDropDownHorizontalOffset(j);
      int j = this.mDropDownAnchor.getWidth();
      int k = rect.left;
      int m = rect.right;
      this.mSearchSrcTextView.setDropDownWidth(j + k + m + b - i);
    } 
  }
  
  @RestrictTo({RestrictTo.Scope.LIBRARY_GROUP})
  public void clearFocus() {
    this.mClearingFocus = true;
    setImeVisibility(false);
    super.clearFocus();
    this.mSearchSrcTextView.clearFocus();
    this.mClearingFocus = false;
  }
  
  void forceSuggestionQuery() {
    HIDDEN_METHOD_INVOKER.doBeforeTextChanged(this.mSearchSrcTextView);
    HIDDEN_METHOD_INVOKER.doAfterTextChanged(this.mSearchSrcTextView);
  }
  
  public int getImeOptions() {
    return this.mSearchSrcTextView.getImeOptions();
  }
  
  public int getInputType() {
    return this.mSearchSrcTextView.getInputType();
  }
  
  public int getMaxWidth() {
    return this.mMaxWidth;
  }
  
  public CharSequence getQuery() {
    return (CharSequence)this.mSearchSrcTextView.getText();
  }
  
  @Nullable
  public CharSequence getQueryHint() {
    return (this.mQueryHint != null) ? this.mQueryHint : ((this.mSearchable != null && this.mSearchable.getHintId() != 0) ? getContext().getText(this.mSearchable.getHintId()) : this.mDefaultQueryHint);
  }
  
  int getSuggestionCommitIconResId() {
    return this.mSuggestionCommitIconResId;
  }
  
  int getSuggestionRowLayout() {
    return this.mSuggestionRowLayout;
  }
  
  public CursorAdapter getSuggestionsAdapter() {
    return this.mSuggestionsAdapter;
  }
  
  public boolean isIconfiedByDefault() {
    return this.mIconifiedByDefault;
  }
  
  public boolean isIconified() {
    return this.mIconified;
  }
  
  public boolean isQueryRefinementEnabled() {
    return this.mQueryRefinement;
  }
  
  public boolean isSubmitButtonEnabled() {
    return this.mSubmitButtonEnabled;
  }
  
  void launchQuerySearch(int paramInt, String paramString1, String paramString2) {
    Intent intent = createIntent("android.intent.action.SEARCH", (Uri)null, (String)null, paramString2, paramInt, paramString1);
    getContext().startActivity(intent);
  }
  
  public void onActionViewCollapsed() {
    setQuery("", false);
    clearFocus();
    updateViewsVisibility(true);
    this.mSearchSrcTextView.setImeOptions(this.mCollapsedImeOptions);
    this.mExpandedInActionView = false;
  }
  
  public void onActionViewExpanded() {
    if (!this.mExpandedInActionView) {
      this.mExpandedInActionView = true;
      this.mCollapsedImeOptions = this.mSearchSrcTextView.getImeOptions();
      this.mSearchSrcTextView.setImeOptions(this.mCollapsedImeOptions | 0x2000000);
      this.mSearchSrcTextView.setText("");
      setIconified(false);
    } 
  }
  
  void onCloseClicked() {
    if (TextUtils.isEmpty((CharSequence)this.mSearchSrcTextView.getText())) {
      if (this.mIconifiedByDefault && (this.mOnCloseListener == null || !this.mOnCloseListener.onClose())) {
        clearFocus();
        updateViewsVisibility(true);
      } 
      return;
    } 
    this.mSearchSrcTextView.setText("");
    this.mSearchSrcTextView.requestFocus();
    setImeVisibility(true);
  }
  
  protected void onDetachedFromWindow() {
    removeCallbacks(this.mUpdateDrawableStateRunnable);
    post(this.mReleaseCursorRunnable);
    super.onDetachedFromWindow();
  }
  
  boolean onItemClicked(int paramInt1, int paramInt2, String paramString) {
    boolean bool = false;
    if (this.mOnSuggestionListener == null || !this.mOnSuggestionListener.onSuggestionClick(paramInt1)) {
      launchSuggestion(paramInt1, 0, (String)null);
      setImeVisibility(false);
      dismissSuggestions();
      bool = true;
    } 
    return bool;
  }
  
  boolean onItemSelected(int paramInt) {
    if (this.mOnSuggestionListener == null || !this.mOnSuggestionListener.onSuggestionSelect(paramInt)) {
      rewriteQueryFromSuggestion(paramInt);
      return true;
    } 
    return false;
  }
  
  protected void onLayout(boolean paramBoolean, int paramInt1, int paramInt2, int paramInt3, int paramInt4) {
    super.onLayout(paramBoolean, paramInt1, paramInt2, paramInt3, paramInt4);
    if (paramBoolean) {
      getChildBoundsWithinSearchView((View)this.mSearchSrcTextView, this.mSearchSrcTextViewBounds);
      this.mSearchSrtTextViewBoundsExpanded.set(this.mSearchSrcTextViewBounds.left, 0, this.mSearchSrcTextViewBounds.right, paramInt4 - paramInt2);
      if (this.mTouchDelegate == null) {
        this.mTouchDelegate = new UpdatableTouchDelegate(this.mSearchSrtTextViewBoundsExpanded, this.mSearchSrcTextViewBounds, (View)this.mSearchSrcTextView);
        setTouchDelegate(this.mTouchDelegate);
        return;
      } 
    } else {
      return;
    } 
    this.mTouchDelegate.setBounds(this.mSearchSrtTextViewBoundsExpanded, this.mSearchSrcTextViewBounds);
  }
  
  protected void onMeasure(int paramInt1, int paramInt2) {
    if (isIconified()) {
      super.onMeasure(paramInt1, paramInt2);
      return;
    } 
    int i = View.MeasureSpec.getMode(paramInt1);
    int j = View.MeasureSpec.getSize(paramInt1);
    switch (i) {
      default:
        paramInt1 = j;
        j = View.MeasureSpec.getMode(paramInt2);
        paramInt2 = View.MeasureSpec.getSize(paramInt2);
        switch (j) {
          default:
            super.onMeasure(View.MeasureSpec.makeMeasureSpec(paramInt1, 1073741824), View.MeasureSpec.makeMeasureSpec(paramInt2, 1073741824));
            return;
          case -2147483648:
            paramInt2 = Math.min(getPreferredHeight(), paramInt2);
          case 0:
            break;
        } 
        break;
      case -2147483648:
        if (this.mMaxWidth > 0) {
          paramInt1 = Math.min(this.mMaxWidth, j);
        } else {
          paramInt1 = Math.min(getPreferredWidth(), j);
        } 
        j = View.MeasureSpec.getMode(paramInt2);
        paramInt2 = View.MeasureSpec.getSize(paramInt2);
        switch (j) {
          default:
            super.onMeasure(View.MeasureSpec.makeMeasureSpec(paramInt1, 1073741824), View.MeasureSpec.makeMeasureSpec(paramInt2, 1073741824));
            return;
          case -2147483648:
            paramInt2 = Math.min(getPreferredHeight(), paramInt2);
          case 0:
            break;
        } 
        break;
      case 1073741824:
        paramInt1 = j;
        if (this.mMaxWidth > 0)
          paramInt1 = Math.min(this.mMaxWidth, j); 
        j = View.MeasureSpec.getMode(paramInt2);
        paramInt2 = View.MeasureSpec.getSize(paramInt2);
        switch (j) {
          default:
            super.onMeasure(View.MeasureSpec.makeMeasureSpec(paramInt1, 1073741824), View.MeasureSpec.makeMeasureSpec(paramInt2, 1073741824));
            return;
          case -2147483648:
            paramInt2 = Math.min(getPreferredHeight(), paramInt2);
          case 0:
            break;
        } 
        break;
      case 0:
        if (this.mMaxWidth > 0) {
          paramInt1 = this.mMaxWidth;
        } else {
          paramInt1 = getPreferredWidth();
        } 
        j = View.MeasureSpec.getMode(paramInt2);
        paramInt2 = View.MeasureSpec.getSize(paramInt2);
        switch (j) {
          default:
            super.onMeasure(View.MeasureSpec.makeMeasureSpec(paramInt1, 1073741824), View.MeasureSpec.makeMeasureSpec(paramInt2, 1073741824));
            return;
          case -2147483648:
            paramInt2 = Math.min(getPreferredHeight(), paramInt2);
          case 0:
            break;
        } 
        break;
    } 
    paramInt2 = getPreferredHeight();
  }
  
  void onQueryRefine(CharSequence paramCharSequence) {
    setQuery(paramCharSequence);
  }
  
  protected void onRestoreInstanceState(Parcelable paramParcelable) {
    if (!(paramParcelable instanceof SavedState)) {
      super.onRestoreInstanceState(paramParcelable);
      return;
    } 
    SavedState savedState = (SavedState)paramParcelable;
    super.onRestoreInstanceState(savedState.getSuperState());
    updateViewsVisibility(savedState.isIconified);
    requestLayout();
  }
  
  protected Parcelable onSaveInstanceState() {
    SavedState savedState = new SavedState(super.onSaveInstanceState());
    savedState.isIconified = isIconified();
    return (Parcelable)savedState;
  }
  
  void onSearchClicked() {
    updateViewsVisibility(false);
    this.mSearchSrcTextView.requestFocus();
    setImeVisibility(true);
    if (this.mOnSearchClickListener != null)
      this.mOnSearchClickListener.onClick((View)this); 
  }
  
  void onSubmitQuery() {
    Editable editable = this.mSearchSrcTextView.getText();
    if (editable != null && TextUtils.getTrimmedLength((CharSequence)editable) > 0 && (this.mOnQueryChangeListener == null || !this.mOnQueryChangeListener.onQueryTextSubmit(editable.toString()))) {
      if (this.mSearchable != null)
        launchQuerySearch(0, (String)null, editable.toString()); 
      setImeVisibility(false);
      dismissSuggestions();
    } 
  }
  
  boolean onSuggestionsKey(View paramView, int paramInt, KeyEvent paramKeyEvent) {
    boolean bool1 = false;
    if (this.mSearchable == null)
      return bool1; 
    boolean bool2 = bool1;
    if (this.mSuggestionsAdapter != null) {
      bool2 = bool1;
      if (paramKeyEvent.getAction() == 0) {
        bool2 = bool1;
        if (KeyEventCompat.hasNoModifiers(paramKeyEvent)) {
          if (paramInt == 66 || paramInt == 84 || paramInt == 61)
            return onItemClicked(this.mSearchSrcTextView.getListSelection(), 0, (String)null); 
          if (paramInt == 21 || paramInt == 22) {
            if (paramInt == 21) {
              paramInt = 0;
            } else {
              paramInt = this.mSearchSrcTextView.length();
            } 
            this.mSearchSrcTextView.setSelection(paramInt);
            this.mSearchSrcTextView.setListSelection(0);
            this.mSearchSrcTextView.clearListSelection();
            HIDDEN_METHOD_INVOKER.ensureImeVisible(this.mSearchSrcTextView, true);
            return true;
          } 
          bool2 = bool1;
          if (paramInt == 19) {
            bool2 = bool1;
            if (this.mSearchSrcTextView.getListSelection() == 0)
              bool2 = bool1; 
          } 
        } 
      } 
    } 
    return bool2;
  }
  
  void onTextChanged(CharSequence paramCharSequence) {
    boolean bool2;
    boolean bool1 = true;
    Editable editable = this.mSearchSrcTextView.getText();
    this.mUserQuery = (CharSequence)editable;
    if (!TextUtils.isEmpty((CharSequence)editable)) {
      bool2 = true;
    } else {
      bool2 = false;
    } 
    updateSubmitButton(bool2);
    if (!bool2) {
      bool2 = bool1;
    } else {
      bool2 = false;
    } 
    updateVoiceButton(bool2);
    updateCloseButton();
    updateSubmitArea();
    if (this.mOnQueryChangeListener != null && !TextUtils.equals(paramCharSequence, this.mOldQueryText))
      this.mOnQueryChangeListener.onQueryTextChange(paramCharSequence.toString()); 
    this.mOldQueryText = paramCharSequence.toString();
  }
  
  void onTextFocusChanged() {
    updateViewsVisibility(isIconified());
    postUpdateFocusedState();
    if (this.mSearchSrcTextView.hasFocus())
      forceSuggestionQuery(); 
  }
  
  void onVoiceClicked() {
    if (this.mSearchable != null) {
      SearchableInfo searchableInfo = this.mSearchable;
      try {
        if (searchableInfo.getVoiceSearchLaunchWebSearch()) {
          Intent intent = createVoiceWebSearchIntent(this.mVoiceWebSearchIntent, searchableInfo);
          getContext().startActivity(intent);
          return;
        } 
      } catch (ActivityNotFoundException activityNotFoundException) {
        Log.w("SearchView", "Could not find voice search activity");
        return;
      } 
      if (activityNotFoundException.getVoiceSearchLaunchRecognizer()) {
        Intent intent = createVoiceAppSearchIntent(this.mVoiceAppSearchIntent, (SearchableInfo)activityNotFoundException);
        getContext().startActivity(intent);
      } 
    } 
  }
  
  public void onWindowFocusChanged(boolean paramBoolean) {
    super.onWindowFocusChanged(paramBoolean);
    postUpdateFocusedState();
  }
  
  @RestrictTo({RestrictTo.Scope.LIBRARY_GROUP})
  public boolean requestFocus(int paramInt, Rect paramRect) {
    if (this.mClearingFocus)
      return false; 
    if (!isFocusable())
      return false; 
    if (!isIconified()) {
      boolean bool2 = this.mSearchSrcTextView.requestFocus(paramInt, paramRect);
      boolean bool1 = bool2;
      if (bool2) {
        updateViewsVisibility(false);
        bool1 = bool2;
      } 
      return bool1;
    } 
    return super.requestFocus(paramInt, paramRect);
  }
  
  @RestrictTo({RestrictTo.Scope.LIBRARY_GROUP})
  public void setAppSearchData(Bundle paramBundle) {
    this.mAppSearchData = paramBundle;
  }
  
  public void setIconified(boolean paramBoolean) {
    if (paramBoolean) {
      onCloseClicked();
      return;
    } 
    onSearchClicked();
  }
  
  public void setIconifiedByDefault(boolean paramBoolean) {
    if (this.mIconifiedByDefault != paramBoolean) {
      this.mIconifiedByDefault = paramBoolean;
      updateViewsVisibility(paramBoolean);
      updateQueryHint();
    } 
  }
  
  public void setImeOptions(int paramInt) {
    this.mSearchSrcTextView.setImeOptions(paramInt);
  }
  
  void setImeVisibility(boolean paramBoolean) {
    if (paramBoolean) {
      post(this.mShowImeRunnable);
      return;
    } 
    removeCallbacks(this.mShowImeRunnable);
    InputMethodManager inputMethodManager = (InputMethodManager)getContext().getSystemService("input_method");
    if (inputMethodManager != null)
      inputMethodManager.hideSoftInputFromWindow(getWindowToken(), 0); 
  }
  
  public void setInputType(int paramInt) {
    this.mSearchSrcTextView.setInputType(paramInt);
  }
  
  public void setMaxWidth(int paramInt) {
    this.mMaxWidth = paramInt;
    requestLayout();
  }
  
  public void setOnCloseListener(OnCloseListener paramOnCloseListener) {
    this.mOnCloseListener = paramOnCloseListener;
  }
  
  public void setOnQueryTextFocusChangeListener(View.OnFocusChangeListener paramOnFocusChangeListener) {
    this.mOnQueryTextFocusChangeListener = paramOnFocusChangeListener;
  }
  
  public void setOnQueryTextListener(OnQueryTextListener paramOnQueryTextListener) {
    this.mOnQueryChangeListener = paramOnQueryTextListener;
  }
  
  public void setOnSearchClickListener(View.OnClickListener paramOnClickListener) {
    this.mOnSearchClickListener = paramOnClickListener;
  }
  
  public void setOnSuggestionListener(OnSuggestionListener paramOnSuggestionListener) {
    this.mOnSuggestionListener = paramOnSuggestionListener;
  }
  
  public void setQuery(CharSequence paramCharSequence, boolean paramBoolean) {
    this.mSearchSrcTextView.setText(paramCharSequence);
    if (paramCharSequence != null) {
      this.mSearchSrcTextView.setSelection(this.mSearchSrcTextView.length());
      this.mUserQuery = paramCharSequence;
    } 
    if (paramBoolean && !TextUtils.isEmpty(paramCharSequence))
      onSubmitQuery(); 
  }
  
  public void setQueryHint(@Nullable CharSequence paramCharSequence) {
    this.mQueryHint = paramCharSequence;
    updateQueryHint();
  }
  
  public void setQueryRefinementEnabled(boolean paramBoolean) {
    this.mQueryRefinement = paramBoolean;
    if (this.mSuggestionsAdapter instanceof SuggestionsAdapter) {
      boolean bool;
      SuggestionsAdapter suggestionsAdapter = (SuggestionsAdapter)this.mSuggestionsAdapter;
      if (paramBoolean) {
        bool = true;
      } else {
        bool = true;
      } 
      suggestionsAdapter.setQueryRefinement(bool);
    } 
  }
  
  public void setSearchableInfo(SearchableInfo paramSearchableInfo) {
    this.mSearchable = paramSearchableInfo;
    if (this.mSearchable != null) {
      updateSearchAutoComplete();
      updateQueryHint();
    } 
    this.mVoiceButtonEnabled = hasVoiceSearch();
    if (this.mVoiceButtonEnabled)
      this.mSearchSrcTextView.setPrivateImeOptions("nm"); 
    updateViewsVisibility(isIconified());
  }
  
  public void setSubmitButtonEnabled(boolean paramBoolean) {
    this.mSubmitButtonEnabled = paramBoolean;
    updateViewsVisibility(isIconified());
  }
  
  public void setSuggestionsAdapter(CursorAdapter paramCursorAdapter) {
    this.mSuggestionsAdapter = paramCursorAdapter;
    this.mSearchSrcTextView.setAdapter((ListAdapter)this.mSuggestionsAdapter);
  }
  
  void updateFocusedState() {
    int[] arrayOfInt;
    if (this.mSearchSrcTextView.hasFocus()) {
      arrayOfInt = FOCUSED_STATE_SET;
    } else {
      arrayOfInt = EMPTY_STATE_SET;
    } 
    Drawable drawable = this.mSearchPlate.getBackground();
    if (drawable != null)
      drawable.setState(arrayOfInt); 
    drawable = this.mSubmitArea.getBackground();
    if (drawable != null)
      drawable.setState(arrayOfInt); 
    invalidate();
  }
  
  private static class AutoCompleteTextViewReflector {
    private Method doAfterTextChanged;
    
    private Method doBeforeTextChanged;
    
    private Method ensureImeVisible;
    
    private Method showSoftInputUnchecked;
    
    AutoCompleteTextViewReflector() {
      try {
        this.doBeforeTextChanged = AutoCompleteTextView.class.getDeclaredMethod("doBeforeTextChanged", new Class[0]);
        this.doBeforeTextChanged.setAccessible(true);
      } catch (NoSuchMethodException noSuchMethodException) {}
      try {
        this.doAfterTextChanged = AutoCompleteTextView.class.getDeclaredMethod("doAfterTextChanged", new Class[0]);
        this.doAfterTextChanged.setAccessible(true);
      } catch (NoSuchMethodException noSuchMethodException) {}
      try {
        this.ensureImeVisible = AutoCompleteTextView.class.getMethod("ensureImeVisible", new Class[] { boolean.class });
        this.ensureImeVisible.setAccessible(true);
      } catch (NoSuchMethodException noSuchMethodException) {}
      try {
        this.showSoftInputUnchecked = InputMethodManager.class.getMethod("showSoftInputUnchecked", new Class[] { int.class, ResultReceiver.class });
        this.showSoftInputUnchecked.setAccessible(true);
      } catch (NoSuchMethodException noSuchMethodException) {}
    }
    
    void doAfterTextChanged(AutoCompleteTextView param1AutoCompleteTextView) {
      if (this.doAfterTextChanged != null)
        try {
          this.doAfterTextChanged.invoke(param1AutoCompleteTextView, new Object[0]);
        } catch (Exception exception) {} 
    }
    
    void doBeforeTextChanged(AutoCompleteTextView param1AutoCompleteTextView) {
      if (this.doBeforeTextChanged != null)
        try {
          this.doBeforeTextChanged.invoke(param1AutoCompleteTextView, new Object[0]);
        } catch (Exception exception) {} 
    }
    
    void ensureImeVisible(AutoCompleteTextView param1AutoCompleteTextView, boolean param1Boolean) {
      if (this.ensureImeVisible != null)
        try {
          this.ensureImeVisible.invoke(param1AutoCompleteTextView, new Object[] { Boolean.valueOf(param1Boolean) });
        } catch (Exception exception) {} 
    }
    
    void showSoftInputUnchecked(InputMethodManager param1InputMethodManager, View param1View, int param1Int) {
      if (this.showSoftInputUnchecked != null)
        try {
          this.showSoftInputUnchecked.invoke(param1InputMethodManager, new Object[] { Integer.valueOf(param1Int), null });
          return;
        } catch (Exception exception) {} 
      param1InputMethodManager.showSoftInput(param1View, param1Int);
    }
  }
  
  public static interface OnCloseListener {
    boolean onClose();
  }
  
  public static interface OnQueryTextListener {
    boolean onQueryTextChange(String param1String);
    
    boolean onQueryTextSubmit(String param1String);
  }
  
  public static interface OnSuggestionListener {
    boolean onSuggestionClick(int param1Int);
    
    boolean onSuggestionSelect(int param1Int);
  }
  
  static class SavedState extends AbsSavedState {
    public static final Parcelable.Creator<SavedState> CREATOR = ParcelableCompat.newCreator(new ParcelableCompatCreatorCallbacks<SavedState>() {
          public SearchView.SavedState createFromParcel(Parcel param2Parcel, ClassLoader param2ClassLoader) {
            return new SearchView.SavedState(param2Parcel, param2ClassLoader);
          }
          
          public SearchView.SavedState[] newArray(int param2Int) {
            return new SearchView.SavedState[param2Int];
          }
        });
    
    boolean isIconified;
    
    public SavedState(Parcel param1Parcel, ClassLoader param1ClassLoader) {
      super(param1Parcel, param1ClassLoader);
      this.isIconified = ((Boolean)param1Parcel.readValue(null)).booleanValue();
    }
    
    SavedState(Parcelable param1Parcelable) {
      super(param1Parcelable);
    }
    
    public String toString() {
      return "SearchView.SavedState{" + Integer.toHexString(System.identityHashCode(this)) + " isIconified=" + this.isIconified + "}";
    }
    
    public void writeToParcel(Parcel param1Parcel, int param1Int) {
      super.writeToParcel(param1Parcel, param1Int);
      param1Parcel.writeValue(Boolean.valueOf(this.isIconified));
    }
  }
  
  static final class null implements ParcelableCompatCreatorCallbacks<SavedState> {
    public SearchView.SavedState createFromParcel(Parcel param1Parcel, ClassLoader param1ClassLoader) {
      return new SearchView.SavedState(param1Parcel, param1ClassLoader);
    }
    
    public SearchView.SavedState[] newArray(int param1Int) {
      return new SearchView.SavedState[param1Int];
    }
  }
  
  @RestrictTo({RestrictTo.Scope.LIBRARY_GROUP})
  public static class SearchAutoComplete extends AppCompatAutoCompleteTextView {
    private SearchView mSearchView;
    
    private int mThreshold = getThreshold();
    
    public SearchAutoComplete(Context param1Context) {
      this(param1Context, (AttributeSet)null);
    }
    
    public SearchAutoComplete(Context param1Context, AttributeSet param1AttributeSet) {
      this(param1Context, param1AttributeSet, R.attr.autoCompleteTextViewStyle);
    }
    
    public SearchAutoComplete(Context param1Context, AttributeSet param1AttributeSet, int param1Int) {
      super(param1Context, param1AttributeSet, param1Int);
    }
    
    private int getSearchViewTextMinWidthDp() {
      Configuration configuration = getResources().getConfiguration();
      null = ConfigurationHelper.getScreenWidthDp(getResources());
      int i = ConfigurationHelper.getScreenHeightDp(getResources());
      return (null >= 960 && i >= 720 && configuration.orientation == 2) ? 256 : ((null >= 600 || (null >= 640 && i >= 480)) ? 192 : 160);
    }
    
    private boolean isEmpty() {
      return (TextUtils.getTrimmedLength((CharSequence)getText()) == 0);
    }
    
    public boolean enoughToFilter() {
      return (this.mThreshold <= 0 || super.enoughToFilter());
    }
    
    protected void onFinishInflate() {
      super.onFinishInflate();
      DisplayMetrics displayMetrics = getResources().getDisplayMetrics();
      setMinWidth((int)TypedValue.applyDimension(1, getSearchViewTextMinWidthDp(), displayMetrics));
    }
    
    protected void onFocusChanged(boolean param1Boolean, int param1Int, Rect param1Rect) {
      super.onFocusChanged(param1Boolean, param1Int, param1Rect);
      this.mSearchView.onTextFocusChanged();
    }
    
    public boolean onKeyPreIme(int param1Int, KeyEvent param1KeyEvent) {
      boolean bool = true;
      if (param1Int == 4) {
        if (param1KeyEvent.getAction() == 0 && param1KeyEvent.getRepeatCount() == 0) {
          KeyEvent.DispatcherState dispatcherState = getKeyDispatcherState();
          boolean bool1 = bool;
          if (dispatcherState != null) {
            dispatcherState.startTracking(param1KeyEvent, this);
            bool1 = bool;
          } 
          return bool1;
        } 
        if (param1KeyEvent.getAction() == 1) {
          KeyEvent.DispatcherState dispatcherState = getKeyDispatcherState();
          if (dispatcherState != null)
            dispatcherState.handleUpEvent(param1KeyEvent); 
          if (param1KeyEvent.isTracking() && !param1KeyEvent.isCanceled()) {
            this.mSearchView.clearFocus();
            this.mSearchView.setImeVisibility(false);
            return bool;
          } 
        } 
      } 
      return super.onKeyPreIme(param1Int, param1KeyEvent);
    }
    
    public void onWindowFocusChanged(boolean param1Boolean) {
      super.onWindowFocusChanged(param1Boolean);
      if (param1Boolean && this.mSearchView.hasFocus() && getVisibility() == 0) {
        ((InputMethodManager)getContext().getSystemService("input_method")).showSoftInput((View)this, 0);
        if (SearchView.isLandscapeMode(getContext()))
          SearchView.HIDDEN_METHOD_INVOKER.ensureImeVisible(this, true); 
      } 
    }
    
    public void performCompletion() {}
    
    protected void replaceText(CharSequence param1CharSequence) {}
    
    void setSearchView(SearchView param1SearchView) {
      this.mSearchView = param1SearchView;
    }
    
    public void setThreshold(int param1Int) {
      super.setThreshold(param1Int);
      this.mThreshold = param1Int;
    }
  }
  
  private static class UpdatableTouchDelegate extends TouchDelegate {
    private final Rect mActualBounds;
    
    private boolean mDelegateTargeted;
    
    private final View mDelegateView;
    
    private final int mSlop;
    
    private final Rect mSlopBounds;
    
    private final Rect mTargetBounds;
    
    public UpdatableTouchDelegate(Rect param1Rect1, Rect param1Rect2, View param1View) {
      super(param1Rect1, param1View);
      this.mSlop = ViewConfiguration.get(param1View.getContext()).getScaledTouchSlop();
      this.mTargetBounds = new Rect();
      this.mSlopBounds = new Rect();
      this.mActualBounds = new Rect();
      setBounds(param1Rect1, param1Rect2);
      this.mDelegateView = param1View;
    }
    
    public boolean onTouchEvent(MotionEvent param1MotionEvent) {
      // Byte code:
      //   0: aload_1
      //   1: invokevirtual getX : ()F
      //   4: f2i
      //   5: istore_2
      //   6: aload_1
      //   7: invokevirtual getY : ()F
      //   10: f2i
      //   11: istore_3
      //   12: iconst_0
      //   13: istore #4
      //   15: iconst_1
      //   16: istore #5
      //   18: iconst_0
      //   19: istore #6
      //   21: aload_1
      //   22: invokevirtual getAction : ()I
      //   25: tableswitch default -> 56, 0 -> 119, 1 -> 150, 2 -> 150, 3 -> 199
      //   56: iload #5
      //   58: istore #7
      //   60: iload #4
      //   62: ifeq -> 116
      //   65: iload #7
      //   67: ifeq -> 217
      //   70: aload_0
      //   71: getfield mActualBounds : Landroid/graphics/Rect;
      //   74: iload_2
      //   75: iload_3
      //   76: invokevirtual contains : (II)Z
      //   79: ifne -> 217
      //   82: aload_1
      //   83: aload_0
      //   84: getfield mDelegateView : Landroid/view/View;
      //   87: invokevirtual getWidth : ()I
      //   90: iconst_2
      //   91: idiv
      //   92: i2f
      //   93: aload_0
      //   94: getfield mDelegateView : Landroid/view/View;
      //   97: invokevirtual getHeight : ()I
      //   100: iconst_2
      //   101: idiv
      //   102: i2f
      //   103: invokevirtual setLocation : (FF)V
      //   106: aload_0
      //   107: getfield mDelegateView : Landroid/view/View;
      //   110: aload_1
      //   111: invokevirtual dispatchTouchEvent : (Landroid/view/MotionEvent;)Z
      //   114: istore #6
      //   116: iload #6
      //   118: ireturn
      //   119: iload #5
      //   121: istore #7
      //   123: aload_0
      //   124: getfield mTargetBounds : Landroid/graphics/Rect;
      //   127: iload_2
      //   128: iload_3
      //   129: invokevirtual contains : (II)Z
      //   132: ifeq -> 60
      //   135: aload_0
      //   136: iconst_1
      //   137: putfield mDelegateTargeted : Z
      //   140: iconst_1
      //   141: istore #4
      //   143: iload #5
      //   145: istore #7
      //   147: goto -> 60
      //   150: aload_0
      //   151: getfield mDelegateTargeted : Z
      //   154: istore #8
      //   156: iload #5
      //   158: istore #7
      //   160: iload #8
      //   162: istore #4
      //   164: iload #8
      //   166: ifeq -> 60
      //   169: iload #5
      //   171: istore #7
      //   173: iload #8
      //   175: istore #4
      //   177: aload_0
      //   178: getfield mSlopBounds : Landroid/graphics/Rect;
      //   181: iload_2
      //   182: iload_3
      //   183: invokevirtual contains : (II)Z
      //   186: ifne -> 60
      //   189: iconst_0
      //   190: istore #7
      //   192: iload #8
      //   194: istore #4
      //   196: goto -> 60
      //   199: aload_0
      //   200: getfield mDelegateTargeted : Z
      //   203: istore #4
      //   205: aload_0
      //   206: iconst_0
      //   207: putfield mDelegateTargeted : Z
      //   210: iload #5
      //   212: istore #7
      //   214: goto -> 60
      //   217: aload_1
      //   218: iload_2
      //   219: aload_0
      //   220: getfield mActualBounds : Landroid/graphics/Rect;
      //   223: getfield left : I
      //   226: isub
      //   227: i2f
      //   228: iload_3
      //   229: aload_0
      //   230: getfield mActualBounds : Landroid/graphics/Rect;
      //   233: getfield top : I
      //   236: isub
      //   237: i2f
      //   238: invokevirtual setLocation : (FF)V
      //   241: goto -> 106
    }
    
    public void setBounds(Rect param1Rect1, Rect param1Rect2) {
      this.mTargetBounds.set(param1Rect1);
      this.mSlopBounds.set(param1Rect1);
      this.mSlopBounds.inset(-this.mSlop, -this.mSlop);
      this.mActualBounds.set(param1Rect2);
    }
  }
}


/* Location:              /opt/home/jayk-storage/Downloads/quadstellar/classes-dex2jar.jar!/android/support/v7/widget/SearchView.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */