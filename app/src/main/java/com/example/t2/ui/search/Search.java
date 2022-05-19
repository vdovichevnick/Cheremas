package com.example.t2.ui.search;


import android.os.Bundle;
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
import androidx.lifecycle.ViewModelProvider;

import com.example.t2.R;
//import com.example.t2.databinding.FragmentDashboardBinding;
import com.example.t2.ui.CustomArrayAdapter;
import com.example.t2.ui.ListItemClass;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Search extends Fragment {
    private Document doc;
    private Thread secTread;
    private Runnable runnable;
    private ListView listView;
    private CustomArrayAdapter adapter;
    private List<ListItemClass> arrayList;
    private EditText userText;
    private Button butText;
    private TextView textStock1;

    //private FragmentDashboardBinding binding;
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        SearchViewModel searchViewModel =
                new ViewModelProvider(this).get(SearchViewModel.class);

        //binding = FragmentDashboardBinding.inflate(inflater, container, false);
        View v = inflater.inflate(R.layout.fragment_search, container,false);
        userText=v.findViewById(R.id.userText);
        butText=v.findViewById(R.id.butText);
        textStock1=v.findViewById(R.id.textStock1);
        butText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(userText.getText().toString().trim().equals("")){
                    Toast.makeText(getActivity().getApplicationContext(),R.string.noUserInput,Toast.LENGTH_LONG).show();
                } else {
                    String ticker=userText.getText().toString();
                    textStock1.setText("Stock searching...");
                    init(ticker,v);

                }
            }
        });
        return v;
    }
    private void init(String s, View v){

        listView = v.findViewById(R.id.listView);
        arrayList = new ArrayList<>();
        adapter = new CustomArrayAdapter(getActivity().getApplicationContext(),R.layout.list_item_1,arrayList,getLayoutInflater());
        listView.setAdapter(adapter);
        runnable=new Runnable() {
            @Override
            public void run() {
                getWeb(s);
            }
        };
        secTread=new Thread(runnable);
        secTread.start();

    }
    private void getWeb(String s){
        try {
            doc = Jsoup.connect("https://finviz.com/quote.ashx?t="+s).get();
            Elements tables = doc.getElementsByTag("tbody");
            Element our_table = tables.get(8); //таблица с показателями
            Element companyTableName = tables.get(6); //таблица с именем
            //Elements elements_from_table=our_table.children(); // разбить таблицу по строкам
            Elements companyName=companyTableName.children();
            getActivity().runOnUiThread(new Runnable() { // адаптер нельзя трогать с второстепенного потока
                @Override
                public void run() {
                    textStock1.setText(companyName.get(1).text().toString()); // обновить информацию
                }
            });
            for (int j=0;j<11;j=j+4) {
                for (int i=0;i<12;i++){
                    ListItemClass items = new ListItemClass();
                    items.setData1(our_table.children().get(i).child(0+j).text());
                    items.setData2(our_table.children().get(i).child(1+j).text());
                    items.setData3(our_table.children().get(i).child(2+j).text());
                    items.setData4(our_table.children().get(i).child(3+j).text());
                    arrayList.add(items); //закинуть в адаптер
                }
            }
            getActivity().runOnUiThread(new Runnable() { // адаптер нельзя трогать с второстепенного потока
                @Override
                public void run() {
                    adapter.notifyDataSetChanged(); // обновить информацию
                }
            });
        } catch (IOException e){
            getActivity().runOnUiThread(new Runnable() { // адаптер нельзя трогать с второстепенного потока
                @Override
                public void run() {
                    textStock1.setText("Error! Company not found."); // обновить информацию
                }
            });
            e.printStackTrace();
        }
    }
/*
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

 */
}