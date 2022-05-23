package com.example.t2.ui.porfolio;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.t2.R;
import com.example.t2.ui.CustomArrayAdapter;
import com.example.t2.ui.ListItemClass;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

public class PorfolioFragment extends Fragment {
    private Document doc;
    private Thread secTread;
    private Runnable runnable;
    private ListView listView;
    private CustomArrayAdapter adapter;
    private List<ListItemClass> arrayList;
    private EditText userText;
    private Button butText;
    private Button deleteSP;
    private TextView textStock1;
    private TextView textView1;
    private TextView textView2;
    private TextView textView3;
    private TextView textView4;
    private TextView textView5;
    private TextView textView6;
    SharedPreferences numStocks;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_portfolio, container, false);
        userText = v.findViewById(R.id.userText);
        butText = v.findViewById(R.id.butText);
        deleteSP = v.findViewById(R.id.deleteSP);
        textStock1 = v.findViewById(R.id.textStock1);
        textView1 = v.findViewById(R.id.textView1);
        textView2 = v.findViewById(R.id.textView2);
        textView3 = v.findViewById(R.id.textView3);
        textView4 = v.findViewById(R.id.textView4);
        textView5 = v.findViewById(R.id.textView5);
        textView6 = v.findViewById(R.id.textView6);
        listView = v.findViewById(R.id.listView);
        arrayList = new ArrayList<>();
        adapter = new CustomArrayAdapter(getActivity().getApplicationContext(), R.layout.list_item_1, arrayList, getLayoutInflater());
        listView.setAdapter(adapter);
        textStock1.setText(getString(R.string.lp));
        float numbers[] = new float [7];
        Set<String> tickerSet= loadNum(numbers);
        settingText(numbers);
        butText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (userText.getText().toString().trim().equals("")) {
                    Toast.makeText(getActivity().getApplicationContext(), R.string.noUserInput, Toast.LENGTH_LONG).show();
                } else {
                    String ticker = userText.getText().toString();
                    textStock1.setText(R.string.stockSearching);
                    init(ticker, v, numbers, tickerSet);
                }
            }
        });
        deleteSP.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Arrays.fill(numbers,0);
                tickerSet.clear();
                settingText(numbers);
                numStocks=getActivity().getPreferences(Context.MODE_PRIVATE);
                SharedPreferences.Editor ed= numStocks.edit();
                ed.clear();
                ed.apply();
                adapter.clear();
                adapter.notifyDataSetChanged();
            }
        });
        return v;
    }
    private void init(String s, View v, float numbers[], Set<String> tickerSet) {
        runnable = new Runnable() {
            @Override
            public void run() {
                getWeb(s,numbers,tickerSet);
            }
        };
        secTread = new Thread(runnable);
        secTread.start();
    }
    private void getWeb(String s, float numbers[], Set<String> tickerSet) {
        try {
            doc = Jsoup.connect("https://finviz.com/quote.ashx?t=" + s).get();
            Elements tables = doc.getElementsByTag("tbody");
            ListItemClass items = new ListItemClass();
            String sector = tables.get(6).children().get(2).children().get(0).children().get(0).text();
            items.setData1(s.toUpperCase());
            items.setData2(sector);
            items.setData3(tables.get(8).children().get(10).children().get(11).text());
            items.setData4(tables.get(8).children().get(11).children().get(11).text());
            arrayList.add(items); //отправить в адаптер
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    textStock1.setText(R.string.atp);
                    tickerSet.add(s);
                    numbers[0]+=1;
                    switch (sector){
                        case  ("Technology"):
                            numbers[1]+=1;
                            break;
                        case ("Healthcare"):
                            numbers[2]+=1;
                            break;
                        case ("Financial"):
                            numbers[3]+=1;
                            break;
                        case ("Energy"):
                            numbers[4]+=1;
                            break;
                        case ("Industrials"):
                            numbers[5]+=1;
                            break;
                        default:
                            numbers[6]+=1;
                            break;
                    }
                    saveNum(numbers,tickerSet);

                    settingText(numbers);
                }
            });

            getActivity().runOnUiThread(new Runnable() { // адаптер нельзя трогать с второстепенного потока
                @Override
                public void run() {
                    adapter.notifyDataSetChanged(); // обновить информацию
                }
            });
        } catch (IOException e) {
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    textStock1.setText(R.string.cnf);
                }
            });
            e.printStackTrace();
        }
    }
    private  void settingText(float numbers[]) {
        String shareT="0",shareH="0",shareF="0",shareE="0",shareI="0",shareO="0";
        if (numbers[0]!=0){
            DecimalFormat df = new DecimalFormat("##.#");
            shareT=df .format(numbers[1]/numbers[0]*100);
            shareH=df .format(numbers[2]/numbers[0]*100);
            shareF=df .format(numbers[3]/numbers[0]*100);
            shareE=df .format(numbers[4]/numbers[0]*100);
            shareI=df .format(numbers[5]/numbers[0]*100);
            shareO=df .format(numbers[6]/numbers[0]*100);
        }
        DecimalFormat df = new DecimalFormat("##.#");
        textView1.setText(getString(R.string.technology)+": " + shareT +"%");
        textView2.setText(getString(R.string.healthcare)+": " + shareH +"%");
        textView3.setText(getString(R.string.financial)+": " + shareF +"%");
        textView4.setText(getString(R.string.energy)+": " + shareE +"%");
        textView5.setText(getString(R.string.industrials)+": " + shareI +"%");
        textView6.setText(getString(R.string.other)+": " + shareO +"%");
    }
    private void saveNum(float numbers[], Set<String> tickerSet) {
        numStocks= getActivity().getPreferences(Context.MODE_PRIVATE);
        SharedPreferences.Editor ed = numStocks.edit();
        ed.putFloat("numAll", numbers[0]);
        ed.putFloat("numTechnology", numbers[1]);
        ed.putFloat("numHealthcare", numbers[2]);
        ed.putFloat("numFinancial", numbers[3]);
        ed.putFloat("numEnergy", numbers[4]);
        ed.putFloat("numIndustrials", numbers[5]);
        ed.putFloat("numOther", numbers[6]);
        ed.putStringSet("ticker", tickerSet);
        ed.apply();
    }
    private Set<String> loadNum(float[] numbers) {
        numStocks= getActivity().getPreferences(Context.MODE_PRIVATE);
        numbers[0]=numStocks.getFloat("numAll",0);
        numbers[1]=numStocks.getFloat("numTechnology", 0);
        numbers[2]=numStocks.getFloat("numHealthcare", 0);
        numbers[3]=numStocks.getFloat("numFinancial", 0);
        numbers[4]=numStocks.getFloat("numEnergy", 0);
        numbers[5]=numStocks.getFloat("numIndustrials", 0);
        numbers[6]=numStocks.getFloat("numOther", 0);
        Set<String> newSet=new TreeSet<>();
        Set<String> tickerSet=new TreeSet<>();
        tickerSet = numStocks.getStringSet("ticker", newSet);
        int i=1;
        for(String r : tickerSet) {
            textStock1.setText(getString(R.string.lp) +" "+ i/numbers[0]*100 + "%");
            connectTickers(r);
            i++;
        }
        textStock1.setText(R.string.wfe);
        return tickerSet;

    }
    private void connectTickers(String s){
        runnable = new Runnable() {
            @Override
            public void run() {
                try {
                    doc = Jsoup.connect("https://finviz.com/quote.ashx?t=" + s).get();
                    Elements tables = doc.getElementsByTag("tbody");
                    ListItemClass items = new ListItemClass();
                    String sector = tables.get(6).children().get(2).children().get(0).children().get(0).text();
                    items.setData1(s.toUpperCase());
                    items.setData2(sector);
                    items.setData3(tables.get(8).children().get(10).children().get(11).text());
                    items.setData4(tables.get(8).children().get(11).children().get(11).text());
                    arrayList.add(items); //отправить в адаптер
                    getActivity().runOnUiThread(new Runnable() { // адаптер нельзя трогать с второстепенного потока
                        @Override
                        public void run() {
                            adapter.notifyDataSetChanged(); // обновить информацию
                        }
                    });
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        };
        secTread = new Thread(runnable);
        secTread.start();
    }
}
