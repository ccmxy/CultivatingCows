package com.cultivatingcows.Adapters;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.cultivatingcows.Models.Game;

import java.util.List;

public class GameAdapter extends BaseAdapter{
        protected Context mContext;
        protected List<Game> mGames;

        public GameAdapter(Context context, List<Game> games) {
            mContext = context;
            mGames = games;
        }

        @Override
        public int getCount() {
            return mGames.size();
        }
//
        @Override
        public Object getItem(int position) {
            return mGames.get(position);
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }
//
        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder holder;

            if (convertView == null) {
                holder = new ViewHolder();
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }

            Game game = mGames.get(position);
            return convertView;
        }

        private static class ViewHolder {
            TextView categoryText;
            TextView runningExpensesSumText;
        }


    }
