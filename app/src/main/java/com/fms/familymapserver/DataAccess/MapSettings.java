package com.fms.familymapserver.DataAccess;

import android.graphics.Color;

import java.util.HashMap;

import static com.google.android.gms.maps.GoogleMap.MAP_TYPE_NORMAL;

public class MapSettings {

    private static int mSpouseLineColor = Color.RED;
    private static int mFamilyTreeLineColor = Color.GREEN;
    private static int mLifeStoryLineColor = Color.BLUE;
    private static int mMapType = MAP_TYPE_NORMAL;
    private static boolean mShowSpouseLine = true;
    private static boolean mShowFamilyTreeLine = true;
    private static boolean mShowLifeStoryLine = true;

    public static int getSpouseLineColor() {
        return mSpouseLineColor;
    }

    public static int getFamilyTreeLineColor() {
        return mFamilyTreeLineColor;
    }

    public static int getLifeStoryLineColor() {
        return mLifeStoryLineColor;
    }

    public static boolean isShowSpouseLine() {
        return mShowSpouseLine;
    }

    public static boolean isShowFamilyTreeLine() {
        return mShowFamilyTreeLine;
    }

    public static boolean isShowLifeStoryLine() {
        return mShowLifeStoryLine;
    }

    public static void setSpouseLineColor(int SpouseLineColor) {
        mSpouseLineColor = SpouseLineColor;
    }

    public static void setFamilyTreeLineColor(int FamilyTreeLineColor) {
        mFamilyTreeLineColor = FamilyTreeLineColor;
    }

    public static void setLifeStoryLineColor(int LifeStoryLineColor) {
        mLifeStoryLineColor = LifeStoryLineColor;
    }

    public static void setShowSpouseLine(boolean ShowSpouseLine) {
        mShowSpouseLine = ShowSpouseLine;
    }

    public static void setShowFamilyTreeLine(boolean ShowFamilyTreeLine) {
        mShowFamilyTreeLine = ShowFamilyTreeLine;
    }

    public static void setShowLifeStoryLine(boolean ShowLifeStoryLine) {
        mShowLifeStoryLine = ShowLifeStoryLine;
    }

    public static int getMapType() {
        return mMapType;
    }

    public static void setMapType(int mMapType) {
        MapSettings.mMapType = mMapType;
    }

    public static void reset()
    {
        mSpouseLineColor = Color.RED;
        mFamilyTreeLineColor = Color.GREEN;
        mLifeStoryLineColor = Color.BLUE;
        mMapType = MAP_TYPE_NORMAL;
        mShowSpouseLine = true;
        mShowFamilyTreeLine = true;
        mShowLifeStoryLine = true;
    }
}
