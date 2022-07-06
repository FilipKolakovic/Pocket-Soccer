package com.example.kf150605d.pocketsoccer;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProvider;
import android.arch.lifecycle.ViewModelProviders;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.kf150605d.pocketsoccer.bazaPodataka.entity.Match;
import com.example.kf150605d.pocketsoccer.model.AppViewModel;

import java.util.ArrayList;
import java.util.List;

public class StatisticActivtiy extends AppCompatActivity {
    public static class MatchesResults{
        public String player1, player2 ;
        public int pOneW, pTwoW, draw;

        public MatchesResults(String player1, String player2, int pOneW, int pTwoW, int draw){
            this.pTwoW = pTwoW;
            this.pOneW = pOneW;
            this.player2 = player2;
            this.player1 = player1;
            this.draw = draw;
        }

        @Override
        public String toString(){
            return player1 + " : " + player2 + "[" + pOneW + " : " + pTwoW + " : " + draw + "]";
        }
    }

    public static class MyAdapter extends RecyclerView.Adapter<MyAdapter.MyHolder> {

        private ArrayList<MatchesResults> myList;

        public MyAdapter(ArrayList<MatchesResults> myList){
            this.myList = myList;
        }

        @NonNull
        @Override
        public MyHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
            LayoutInflater layoutInflater = LayoutInflater.from(viewGroup.getContext());
            View view = layoutInflater.inflate(R.layout.statistic_holder, viewGroup, false);
            return new MyHolder(view);
        }

        @Override
        public int getItemCount() {
            return myList.size() + 1;
        }

        public static class MyHolder extends RecyclerView.ViewHolder{

            public TextView player1;
            public TextView player2;
            public TextView p1win;
            public TextView p2win;
            public TextView draw;
            public MyHolder(@NonNull final View itemView) {
                super(itemView);
                draw = itemView.findViewById(R.id.my_holder_ties);
                p1win = itemView.findViewById(R.id.my_holder_player1wins);
                p2win = itemView.findViewById(R.id.my_holder_player2wins);
                player1 = itemView.findViewById(R.id.my_holder_player1);
                player2 = itemView.findViewById(R.id.my_holder_player2);
            }
        }

        @Override
        public void onBindViewHolder(@NonNull MyHolder myHolder, int i) {
            if(i == 0){
                myHolder.player1.setText("P1");
                myHolder.player2.setText("P2");
                myHolder.p1win.setText("P1Win");
                myHolder.p2win.setText("P2Win");
                myHolder.draw.setText("Draw");
                return;
            }
            MatchesResults current = myList.get(i - 1);

            myHolder.player1.setText(current.player1);
            myHolder.player2.setText(current.player2);
            myHolder.p1win.setText(Integer.toString(current.pOneW));
            myHolder.p2win.setText(Integer.toString(current.pTwoW));
            myHolder.draw.setText(Integer.toString(current.draw));
        }
    }

    private RecyclerView mRecyclerView;
    private MyAdapter mAdapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_statistic_activtiy);

        mRecyclerView = findViewById(R.id.listviewallmatches);
        mRecyclerView.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(linearLayoutManager);

        ViewModelProvider provider = ViewModelProviders.of(this);
        AppViewModel myViewModel = provider.get(AppViewModel.class);

        myViewModel.getAllMatches().observe(this, new Observer<List<Match>>() {
            @Override
            public void onChanged(@Nullable List<Match> matches) {
                if(matches == null) return;

                ArrayList<MatchesResults> list = new ArrayList<>();
                for(Match match : matches){
                    boolean existing = false;

                    for(MatchesResults current : list){
                        if((current.player1.equals(match.getPlayerOne()) && current.player2.equals(match.getPlayerTwo())) ||
                                (current.player2.equals(match.getPlayerOne()) && current.player1.equals(match.getPlayerTwo()))){
                            int outcome = match.getOutcome();
                            if(match.getPlayerOne().equals(current.player1)){
                                switch (outcome){
                                    case 0:
                                        current.draw++; break;
                                    case 1:
                                        current.pOneW++; break;
                                    case 2:
                                        current.pTwoW++; break;
                                }
                            } else{
                                switch (outcome){
                                    case 0:
                                        current.draw++; break;
                                    case 1:
                                        current.pTwoW++; break;
                                    case 2:
                                        current.pOneW++; break;
                                }
                            }
                            existing = true;
                            break;
                        }
                    }

                    if(!existing){
                        int playerOnePoint = match.getPlayerOnePoint();
                        int playerTwoPoint = match.getPlayerTwoPoint();
                        MatchesResults newMatchResult = new MatchesResults(
                                match.getPlayerOne(), match.getPlayerTwo(), 0, 0, 0
                        );
                        if(playerOnePoint > playerTwoPoint) newMatchResult.pOneW++;
                        else if(playerOnePoint < playerTwoPoint) newMatchResult.pTwoW++;
                        else newMatchResult.draw++;

                        list.add(newMatchResult);
                    }
                }
                mAdapter = new MyAdapter(list);
                mRecyclerView.setAdapter(mAdapter);
            }
        });
    }


    public void resetAll(View view) {
        ViewModelProvider provider = ViewModelProviders.of(this);
        AppViewModel mViewModel = provider.get(AppViewModel.class);
        mViewModel.deleteAllMatches();
        mViewModel.deleteAllUsers();
    }
}
