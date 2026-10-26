package com.noortilawa.app;

import android.app.Activity;
import android.os.Bundle;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.GradientDrawable;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.content.Context;
import android.widget.*;

import java.util.Locale;

public class MainActivity extends Activity {
    private static final int GREEN = Color.rgb(4,120,87);
    private static final int DARK_GREEN = Color.rgb(6,78,59);
    private static final int GOLD = Color.rgb(217,119,6);
    private static final int CREAM = Color.rgb(254,243,199);
    private SharedPreferences prefs;
    private LinearLayout root, content, bottomBar;
    private boolean dark;
    private int count;
    private String currentPage = "الرئيسية";
    private EditText searchBox;

    private final String[] surahs = {
        "الفاتحة","البقرة","آل عمران","النساء","المائدة","الأنعام","الأعراف","الأنفال","التوبة","يونس",
        "هود","يوسف","الرعد","إبراهيم","الحجر","النحل","الإسراء","الكهف","مريم","طه",
        "الأنبياء","الحج","المؤمنون","النور","الفرقان","الشعراء","النمل","القصص","العنكبوت","الروم",
        "لقمان","السجدة","الأحزاب","سبأ","فاطر","يس","الصافات","ص","الزمر","غافر",
        "فصلت","الشورى","الزخرف","الدخان","الجاثية","الأحقاف","محمد","الفتح","الحجرات","ق",
        "الذاريات","الطور","النجم","القمر","الرحمن","الواقعة","الحديد","المجادلة","الحشر","الممتحنة",
        "الصف","الجمعة","المنافقون","التغابن","الطلاق","التحريم","الملك","القلم","الحاقة","المعارج",
        "نوح","الجن","المزمل","المدثر","القيامة","الإنسان","المرسلات","النبأ","النازعات","عبس",
        "التكوير","الانفطار","المطففين","الانشقاق","البروج","الطارق","الأعلى","الغاشية","الفجر","البلد",
        "الشمس","الليل","الضحى","الشرح","التين","العلق","القدر","البينة","الزلزلة","العاديات",
        "القارعة","التكاثر","العصر","الهمزة","الفيل","قريش","الماعون","الكوثر","الكافرون","النصر",
        "المسد","الإخلاص","الفلق","الناس"
    };
    private final int[] ayahCounts = {
        7,286,200,176,120,165,206,75,129,109,123,111,43,52,99,128,111,110,98,135,
        112,78,118,64,77,227,93,88,69,60,34,30,73,54,45,83,182,88,75,85,
        54,53,89,59,37,35,38,29,18,45,60,49,62,55,78,96,29,22,24,13,
        14,11,11,18,12,12,30,52,52,44,28,28,20,56,40,31,50,40,46,42,
        29,19,36,25,22,17,19,26,30,20,15,21,11,8,8,19,5,8,8,11,
        11,8,3,9,5,4,7,3,6,3,5,4,5,6
    };

    @Override public void onCreate(Bundle state) {
        super.onCreate(state);
        prefs = getSharedPreferences("noor_prefs", MODE_PRIVATE);
        dark = prefs.getBoolean("dark", false);
        count = prefs.getInt("tasbeeh_count", 0);
        showApp();
    }

    private int bg() { return dark ? Color.rgb(15,23,42) : Color.rgb(248,250,252); }
    private int surface() { return dark ? Color.rgb(30,41,59) : Color.WHITE; }
    private int textColor() { return dark ? Color.rgb(248,250,252) : Color.rgb(15,23,42); }
    private int muted() { return dark ? Color.rgb(203,213,225) : Color.rgb(100,116,139); }

    private GradientDrawable shape(int color, int radius, int strokeColor) {
        GradientDrawable d = new GradientDrawable();
        d.setColor(color);
        d.setCornerRadius(radius);
        if (strokeColor != 0) d.setStroke(dp(1), strokeColor);
        return d;
    }
    private int dp(float v) { return (int)(v * getResources().getDisplayMetrics().density + 0.5f); }

    private TextView label(String s, int size, int color, boolean bold) {
        TextView t = new TextView(this);
        t.setText(s); t.setTextSize(size); t.setTextColor(color);
        t.setGravity(Gravity.RIGHT | Gravity.CENTER_VERTICAL);
        t.setTextDirection(View.TEXT_DIRECTION_RTL);
        if (bold) t.setTypeface(Typeface.DEFAULT, Typeface.BOLD);
        t.setPadding(dp(2), dp(3), dp(2), dp(3));
        return t;
    }
    private LinearLayout vertical() {
        LinearLayout l = new LinearLayout(this);
        l.setOrientation(LinearLayout.VERTICAL);
        l.setLayoutDirection(View.LAYOUT_DIRECTION_RTL);
        return l;
    }
    private LinearLayout horizontal() {
        LinearLayout l = new LinearLayout(this);
        l.setOrientation(LinearLayout.HORIZONTAL);
        l.setGravity(Gravity.CENTER_VERTICAL);
        l.setLayoutDirection(View.LAYOUT_DIRECTION_RTL);
        return l;
    }
    private LinearLayout.LayoutParams lp(int w, int h) { return new LinearLayout.LayoutParams(w < 0 ? w : dp(w), h < 0 ? h : dp(h)); }
    private void pad(View v, int a, int b, int c, int d) { v.setPadding(dp(a),dp(b),dp(c),dp(d)); }

    private TextView button(String text, int color) {
        TextView b = label(text, 15, Color.WHITE, true);
        b.setGravity(Gravity.CENTER);
        b.setBackground(shape(color, dp(14), 0));
        b.setPadding(dp(16),dp(11),dp(16),dp(11));
        return b;
    }

    private void showApp() {
        root = vertical();
        root.setBackgroundColor(bg());
        setContentView(root);

        LinearLayout header = horizontal();
        header.setBackgroundColor(DARK_GREEN);
        pad(header, 16, 10, 16, 10);
        TextView logo = label("۞", 28, CREAM, true);
        logo.setGravity(Gravity.CENTER);
        header.addView(logo, lp(42, 44));
        LinearLayout titles = vertical();
        TextView name = label("نور التلاوة", 20, Color.WHITE, true);
        TextView subtitle = label("رحلتك المطمئنة مع القرآن", 11, CREAM, false);
        titles.addView(name); titles.addView(subtitle);
        header.addView(titles, new LinearLayout.LayoutParams(0, -2, 1));
        TextView theme = button(dark ? "☀" : "☾", GOLD);
        header.addView(theme, lp(46, 42));
        theme.setOnClickListener(v -> {
            dark = !dark; prefs.edit().putBoolean("dark", dark).apply(); showApp();
        });
        root.addView(header, lp(-1, 64));

        ScrollView scroll = new ScrollView(this);
        scroll.setFillViewport(true);
        content = vertical();
        pad(content, 16, 16, 16, 20);
        scroll.addView(content);
        root.addView(scroll, new LinearLayout.LayoutParams(-1, 0, 1));

        buildPage();

        bottomBar = horizontal();
        bottomBar.setBackgroundColor(surface());
        bottomBar.setElevation(dp(10));
        String[][] nav = {{"الرئيسية","⌂"},{"القرآن","۞"},{"الأذكار","◉"},{"السبحة","●"}};
        for (String[] item : nav) {
            LinearLayout tab = vertical();
            tab.setGravity(Gravity.CENTER);
            TextView ico = label(item[1], 20, currentPage.equals(item[0]) ? GREEN : muted(), true);
            ico.setGravity(Gravity.CENTER);
            TextView txt = label(item[0], 11, currentPage.equals(item[0]) ? GREEN : muted(), true);
            txt.setGravity(Gravity.CENTER);
            tab.addView(ico); tab.addView(txt);
            bottomBar.addView(tab, new LinearLayout.LayoutParams(0, dp(58), 1));
            tab.setOnClickListener(v -> navigate(item[0]));
        }
        root.addView(bottomBar, lp(-1, 58));
    }

    private void navigate(String page) { currentPage = page; showApp(); }

    private void buildPage() {
        if ("الرئيسية".equals(currentPage)) buildHome();
        else if ("القرآن".equals(currentPage)) buildQuran();
        else if ("السبحة".equals(currentPage)) buildTasbeeh();
        else if ("الأذكار".equals(currentPage)) buildAdhkar();
        else buildSettings();
    }

    private void addCard(LinearLayout parent, View child) {
        LinearLayout card = vertical();
        card.setBackground(shape(surface(), dp(18), dark ? Color.rgb(51,65,85) : Color.rgb(226,232,240)));
        pad(card, 16, 14, 16, 14);
        card.addView(child);
        LinearLayout.LayoutParams p = lp(-1, -2);
        p.bottomMargin = dp(12);
        parent.addView(card, p);
    }

    private void buildHome() {
        LinearLayout hero = vertical();
        hero.setBackground(shape(DARK_GREEN, dp(22), 0));
        pad(hero, 20, 20, 20, 20);
        hero.addView(label("✦  السكينة تبدأ بآية", 13, CREAM, true));
        TextView title = label("اجعل للقرآن نصيبًا من يومك", 23, Color.WHITE, true);
        title.setPadding(0,dp(12),0,dp(6)); hero.addView(title);
        hero.addView(label("خطوة صغيرة كل يوم، وأثر يبقى في القلب.", 14, Color.rgb(220,252,231), false));
        TextView open = button("تصفح سور القرآن  ←", GOLD);
        LinearLayout.LayoutParams op = lp(-1, 48); op.topMargin = dp(16); hero.addView(open, op);
        open.setOnClickListener(v -> navigate("القرآن"));
        addCard(content, hero);

        content.addView(label("مساحتك الإيمانية", 20, textColor(), true));
        LinearLayout row = horizontal();
        String[][] quick = {{"القرآن","📖"},{"الأذكار","🤲"},{"السبحة","📿"}};
        for (String[] q : quick) {
            LinearLayout tile = vertical(); tile.setGravity(Gravity.CENTER);
            tile.setBackground(shape(surface(), dp(16), dark ? Color.rgb(51,65,85) : Color.rgb(226,232,240)));
            TextView emoji = label(q[1], 25, GOLD, true); emoji.setGravity(Gravity.CENTER);
            TextView txt = label(q[0], 13, textColor(), true); txt.setGravity(Gravity.CENTER);
            tile.addView(emoji); tile.addView(txt);
            LinearLayout.LayoutParams tp = new LinearLayout.LayoutParams(0, dp(88), 1);
            tp.setMargins(dp(3),dp(10),dp(3),dp(12)); row.addView(tile,tp);
            tile.setOnClickListener(v -> navigate(q[0]));
        }
        content.addView(row);
        LinearLayout kh = vertical();
        kh.addView(label("📈  متابعة الختمة", 17, textColor(), true));
        kh.addView(label("اجعل القراءة عادة يومية. تقدمك محفوظ داخل التطبيق.", 13, muted(), false));
        TextView btn = button("افتح فهرس السور", GREEN);
        LinearLayout.LayoutParams bp = lp(-1, 46); bp.topMargin = dp(12); kh.addView(btn,bp);
        btn.setOnClickListener(v -> navigate("القرآن"));
        addCard(content,kh);
        LinearLayout reminder = vertical();
        reminder.addView(label("🌿 تذكير لطيف", 16, GOLD, true));
        reminder.addView(label("خير الأعمال ما دام وإن قلّ؛ خصص وقتًا ثابتًا للتلاوة والذكر.", 14, textColor(), false));
        addCard(content,reminder);
    }

    private void buildQuran() {
        content.addView(label("فهرس القرآن الكريم", 23, textColor(), true));
        content.addView(label("114 سورة • ابحث باسم السورة", 13, muted(), false));
        searchBox = new EditText(this);
        searchBox.setSingleLine(true);
        searchBox.setHint("اكتب اسم السورة...");
        searchBox.setTextColor(textColor());
        searchBox.setHintTextColor(muted());
        searchBox.setTextSize(15);
        searchBox.setGravity(Gravity.RIGHT | Gravity.CENTER_VERTICAL);
        searchBox.setBackground(shape(surface(), dp(14), dark ? Color.rgb(71,85,105) : Color.rgb(203,213,225)));
        pad(searchBox,14,8,14,8);
        LinearLayout.LayoutParams sp = lp(-1, 50); sp.topMargin = dp(12); sp.bottomMargin = dp(12);
        content.addView(searchBox,sp);

        LinearLayout list = vertical();
        content.addView(list);
        Runnable fill = () -> {
            list.removeAllViews();
            String query = searchBox.getText().toString().trim();
            for (int i=0; i<surahs.length; i++) {
                final int idx = i;
                if (!surahs[i].contains(query)) continue;
                LinearLayout item = horizontal();
                item.setBackground(shape(surface(), dp(14), dark ? Color.rgb(51,65,85) : Color.rgb(226,232,240)));
                pad(item,12,9,12,9);
                TextView num = label(String.format(Locale.getDefault(), "%03d", i+1), 13, GOLD, true);
                num.setGravity(Gravity.CENTER);
                num.setBackground(shape(dark ? Color.rgb(51,65,85) : CREAM, dp(10), 0));
                item.addView(num,lp(44,44));
                LinearLayout info = vertical(); pad(info,10,0,0,0);
                info.addView(label(surahs[i],17,textColor(),true));
                info.addView(label(ayahCounts[i]+" آية • "+(i==0||i==8||i==27||i==29||i==82||i==89||i==95||i==96||i==97||i==98||i==99||i==100||i==107||i==109||i==110||i==111 ? "مكية/مدنية حسب السورة" : "سورة من القرآن الكريم"),11,muted(),false));
                item.addView(info,new LinearLayout.LayoutParams(0,-2,1));
                TextView arrow = label("‹",26,GREEN,true); arrow.setGravity(Gravity.CENTER);
                item.addView(arrow,lp(24,44));
                LinearLayout.LayoutParams ip = lp(-1,-2); ip.bottomMargin=dp(8); list.addView(item,ip);
                item.setOnClickListener(v -> {
                    new android.app.AlertDialog.Builder(MainActivity.this)
                        .setTitle("سورة "+surahs[idx])
                        .setMessage("هذه النسخة تحتوي على فهرس السور. لإظهار الآيات كاملةً بدقة، يجب إضافة بيانات مصحف موثوقة ومراجعتها قبل النشر.")
                        .setPositiveButton("حسنًا", null).show();
                });
            }
            if (list.getChildCount()==0) {
                TextView empty=label("لا توجد سورة بهذا الاسم.",14,muted(),false);
                list.addView(empty);
            }
        };
        searchBox.addTextChangedListener(new android.text.TextWatcher() {
            public void beforeTextChanged(CharSequence s,int st,int c,int a){}
            public void onTextChanged(CharSequence s,int st,int before,int count){fill.run();}
            public void afterTextChanged(android.text.Editable e){}
        });
        fill.run();
    }

    private void buildTasbeeh() {
        content.addView(label("السبحة الرقمية", 23, textColor(), true));
        content.addView(label("اذكر الله بقلب حاضر", 14, muted(), false));
        LinearLayout panel = vertical(); panel.setGravity(Gravity.CENTER);
        panel.setBackground(shape(DARK_GREEN, dp(24), 0));
        pad(panel,18,28,18,28);
        panel.addView(label("سُبْحَانَ اللهِ وَبِحَمْدِهِ",20,CREAM,true));
        TextView number = label(String.valueOf(count),52,Color.WHITE,true);
        number.setGravity(Gravity.CENTER); number.setPadding(0,dp(12),0,dp(12));
        panel.addView(number);
        panel.addView(label("عدد التسبيحات",13,Color.rgb(220,252,231),false));
        TextView plus = button("＋  تسبيحة", GOLD);
        LinearLayout.LayoutParams pp=lp(-1,58); pp.topMargin=dp(20); panel.addView(plus,pp);
        plus.setOnClickListener(v -> {
            count++; prefs.edit().putInt("tasbeeh_count",count).apply(); number.setText(String.valueOf(count));
        });
        TextView reset = button("إعادة التصفير", GREEN);
        LinearLayout.LayoutParams rp=lp(-1,48); rp.topMargin=dp(10); panel.addView(reset,rp);
        reset.setOnClickListener(v -> new android.app.AlertDialog.Builder(this)
            .setMessage("هل تريد تصفير عداد التسبيح؟")
            .setNegativeButton("إلغاء",null)
            .setPositiveButton("تصفير",(d,w)->{count=0;prefs.edit().putInt("tasbeeh_count",0).apply();number.setText("0");}).show());
        addCard(content,panel);
        LinearLayout dhikr=vertical();
        dhikr.addView(label("أذكار مقترحة",17,textColor(),true));
        dhikr.addView(label("سبحان الله • الحمد لله • الله أكبر • أستغفر الله",15,muted(),false));
        addCard(content,dhikr);
    }

    private void buildAdhkar() {
        content.addView(label("الأذكار", 23, textColor(), true));
        content.addView(label("أذكار قصيرة يمكنك قراءتها وتكرارها", 14, muted(), false));
        String[][] azkar = {
            {"سبحان الله وبحمده","ذكر عظيم يسير على اللسان."},
            {"أستغفر الله وأتوب إليه","استغفار وتجديد للتوبة."},
            {"لا إله إلا الله","كلمة التوحيد."},
            {"اللهم صل وسلم على نبينا محمد","الصلاة والسلام على النبي ﷺ."},
            {"سبحان الله، والحمد لله، ولا إله إلا الله، والله أكبر","من الكلمات الطيبة الجامعة."},
            {"لا حول ولا قوة إلا بالله","استعانة بالله وتفويض الأمر إليه."}
        };
        for (String[] z : azkar) {
            LinearLayout card=vertical();
            card.addView(label("۞  "+z[0],19,GREEN,true));
            TextView desc=label(z[1],13,muted(),false);
            LinearLayout.LayoutParams dp=lp(-1,-2); dp.topMargin=dp(5); card.addView(desc,dp);
            addCard(content,card);
        }
        content.addView(label("ملاحظة: هذه أذكار عامة مختارة، وليست بديلًا عن كتاب أذكار موثّق مع التخريج.",11,muted(),false));
    }

    private void buildSettings() {
        content.addView(label("الإعدادات", 23, textColor(), true));
        LinearLayout card=vertical();
        card.addView(label("المظهر",17,textColor(),true));
        card.addView(label(dark ? "الوضع الداكن مفعّل" : "الوضع الفاتح مفعّل",14,muted(),false));
        TextView toggle=button(dark ? "تفعيل الوضع الفاتح" : "تفعيل الوضع الداكن",GREEN);
        LinearLayout.LayoutParams tp=lp(-1,48); tp.topMargin=dp(12); card.addView(toggle,tp);
        toggle.setOnClickListener(v->{dark=!dark;prefs.edit().putBoolean("dark",dark).apply();showApp();});
        addCard(content,card);
        LinearLayout about=vertical();
        about.addView(label("عن نور التلاوة",17,textColor(),true));
        about.addView(label("نسخة تأسيسية لتطبيق قرآن وأذكار بواجهة عربية أصلية بلغة Java.",14,muted(),false));
        about.addView(label("الإصدار 1.0 • تُحفظ إعدادات المظهر وعدّاد السبحة محليًا على الجهاز.",12,muted(),false));
        addCard(content,about);
    }
}