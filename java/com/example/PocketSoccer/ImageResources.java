package com.example.kf150605d.pocketsoccer;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.v4.content.res.ResourcesCompat;

public class ImageResources {

    public static Drawable[] allTeams;
    public static Drawable selectedPlayerDrawable;

    public static final int ballDrawable = R.drawable.ball0;
    public static final int goalsResource = R.drawable.golovi;
    private Context mContext;


    public static final int[] teamsPicture = {
            R.drawable.r0, R.drawable.r1, R.drawable.r3, R.drawable.r4,
            R.drawable.r5, R.drawable.r6, R.drawable.r7, R.drawable.r8, R.drawable.r9,
            R.drawable.r10, R.drawable.r11, R.drawable.r12, R.drawable.r13, R.drawable.r14,

    };


    public ImageResources(Context context){mContext = context;}

    public Drawable getPictureOfTeam(String nameOfTeam)
    {
        switch (nameOfTeam)
        {
            case "Hungary":
                return  ResourcesCompat.getDrawable(mContext.getResources(), teamsPicture[0], null);
            case "Algeria":
                return  mContext.getResources().getDrawable(teamsPicture[1]);
            case "Australia":
                return mContext.getResources().getDrawable(teamsPicture[2]);
            case "Brazil":
                return mContext.getResources().getDrawable(teamsPicture[3]);
            case "Canada":
                return mContext.getResources().getDrawable(teamsPicture[4]);
            case "China":
                return mContext.getResources().getDrawable(teamsPicture[5]);
            case "Ireland":
                return mContext.getResources().getDrawable(teamsPicture[6]);
            case "Denmark":
                return mContext.getResources().getDrawable(teamsPicture[7]);
            case "England":
                return mContext.getResources().getDrawable(teamsPicture[8]);
            case "France":
                return mContext.getResources().getDrawable(teamsPicture[9]);
            case "Germany":
                return mContext.getResources().getDrawable(teamsPicture[10]);
            case "Ghana":
                return mContext.getResources().getDrawable(teamsPicture[11]);
            case "Greece":
                return mContext.getResources().getDrawable(teamsPicture[12]);
            case "Honduras":
                return mContext.getResources().getDrawable(teamsPicture[13]);
            default:
                return allTeams[0];
        }
    }


}
