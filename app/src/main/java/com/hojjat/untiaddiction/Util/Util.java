package com.hojjat.untiaddiction.Util;

import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.hojjat.untiaddiction.ActivityMain;
import com.hojjat.untiaddiction.ServiceMain;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * Created by hojjat on 8/22/15.
 */
public class Util {
    private static final String Tag = Util.class.getName();
    private static final String FONTS_PATH = "fonts/";
    private static final String FONTS_EXTENTION = ".ttf";
    private static Map<String, Typeface> fonts = new HashMap<>();

    private static FontFamily appDefaultFontFamily = FontFamily.Naskh;

    public enum FontFamily {
        Naskh("Naskh"),
        Default(Naskh.toString());

        private String text;

        FontFamily(String text) {
            this.text = text;
        }

        @Override
        public String toString() {
            return text;
        }
    }

    public enum FontWeight {
        Bold("Bold"),
        Regular("Regular");


        private String text;

        FontWeight(String text) {
            this.text = text;
        }

        @Override
        public String toString() {
            return text;
        }
    }


    static public void setFont(Context context, FontFamily fontFamily, FontWeight fontWeight, Object... elements) {
        Typeface typeFace = getTypeFace(context, fontFamily, fontWeight);
        setFont(typeFace, elements);
    }

    private static Typeface getTypeFace(Context context, FontFamily fontFamily, FontWeight fontWeight) {
        if (!fonts.containsKey(fontFamily.toString() + fontWeight.toString()))
            fonts.put(fontFamily.toString() + fontWeight.toString(), Typeface.createFromAsset(context.getAssets(), getTypeFacePath(fontFamily, fontWeight)));
        return fonts.get(fontFamily.toString() + fontWeight.toString());
    }

    private static void setFont(Typeface typeface, Object... elements){
        for (Object element : elements) {
            if(element instanceof TextView)
                ((TextView) element).setTypeface(typeface);
            else if (element instanceof Button)
                ((Button) element).setTypeface(typeface);
            else
                Log.e(Tag, "invalid input!");
        }
    }

    private static String getTypeFacePath(FontFamily fontFamily, FontWeight fontWeight) {
        return FONTS_PATH + fontFamily.toString() + "-" + fontWeight.toString() + FONTS_EXTENTION;
    }
    public static void setText(Object elem, String text){
        if(elem instanceof TextView)
            ((TextView) elem).setText(PersianReshape.reshape(text));
        else if (elem instanceof Button)
            ((Button) elem).setText(PersianReshape.reshape(text));
        else
            Log.e(Tag, "invalid input!");
    }

    public static void setText(Object elem0, String text0,Object elem1, String text1){
        setText(elem0, text0);
        setText(elem1, text1);
    }

    public static void setText(Object elem0, String text0,Object elem1, String text1,Object elem2, String text2){
        setText(elem0, text0);
        setText(elem1, text1, elem2, text2);
    }

    public static void setText(Object elem0, String text0,Object elem1, String text1,Object elem2, String text2,Object elem3, String text3){
        setText(elem0, text0);
        setText(elem1, text1, elem2, text2, elem3, text3);
    }

    public static void setText(Object elem0, String text0,Object elem1, String text1,Object elem2, String text2,Object elem3, String text3,Object elem4, String text4){
        setText(elem0, text0);
        setText(elem1, text1, elem2, text2, elem3, text3, elem4, text4);
    }

    public static void setText(Object elem0, String text0,Object elem1, String text1,Object elem2, String text2,Object elem3, String text3,Object elem4, String text4,Object elem5, String text5){
        setText(elem0, text0);
        setText(elem1, text1, elem2, text2, elem3, text3, elem4, text4, elem5, text5);
    }

    public static void setText(Object elem0, String text0,Object elem1, String text1,Object elem2, String text2,Object elem3, String text3,Object elem4, String text4,Object elem5, String text5,Object elem6, String text6){
        setText(elem0, text0);
        setText(elem1, text1, elem2, text2, elem3, text3, elem4, text4, elem5, text5 ,elem6, text6);
    }

    public static void setText(Object elem0, String text0,Object elem1, String text1,Object elem2, String text2,Object elem3, String text3,Object elem4, String text4,Object elem5, String text5,Object elem6, String text6,Object elem7, String text7){
        setText(elem0, text0);
        setText(elem1, text1, elem2, text2, elem3, text3, elem4, text4, elem5, text5 ,elem6, text6, elem7, text7);
    }

    public static boolean isNumeric(String s){
        try {
            Integer.parseInt(s);
        }catch (Exception e){
            return false;
        }
        return true;
    }

    public static void startMainService(Context context) {
        context.startService(new Intent(context, ServiceMain.class));
    }
}