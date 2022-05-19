package com.example.t2.ui.market;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.t2.R;
//import com.example.t2.databinding.FragmentHomeBinding;
import com.example.t2.ui.CustomArrayAdapter;
import com.example.t2.ui.ListItemClass;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class MarketFragment extends Fragment {

    private Document doc1,doc2,doc3,doc4;
    private Thread secTread;
    private Runnable runnable;
    private ListView listView;
    private CustomArrayAdapter adapter;
    private List<ListItemClass> arrayList;

    //private FragmentHomeBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        MarketViewModel marketViewModel =
                new ViewModelProvider(this).get(MarketViewModel.class);

        //binding = FragmentHomeBinding.inflate(inflater, container, false);
        View v = inflater.inflate(R.layout.fragment_market, container,false);

        init(v);


        return v;
    }
    private void init(View v){

        listView = v.findViewById(R.id.listView);
        arrayList = new ArrayList<>();
        adapter = new CustomArrayAdapter(getActivity().getApplicationContext(),R.layout.list_item_1,arrayList,getLayoutInflater());
        listView.setAdapter(adapter);
        runnable=new Runnable() {
            @Override
            public void run() {
                getWeb();
            }
        };
        secTread=new Thread(runnable);
        secTread.start();

    }
    //retrofit
    private void getWeb(){
        try {
            doc1 = Jsoup.connect("https://www.investing.com/indices/mcx").get();
            Elements tables1 = doc1.getElementsByTag("tbody");
            Element our_table1 = tables1.get(5);
            ListItemClass items = new ListItemClass();
            items.setData1("S&P 500");
            items.setData2(our_table1.children().get(3).child(1).text());
            items.setData3(our_table1.children().get(3).child(2).text());
            items.setData4(our_table1.children().get(3).child(3).text());
            arrayList.add(items);
            items = new ListItemClass();
            items.setData1("NASDAQ");
            items.setData2(our_table1.children().get(4).child(1).text());
            items.setData3(our_table1.children().get(4).child(2).text());
            items.setData4(our_table1.children().get(4).child(3).text());
            arrayList.add(items);
            items = new ListItemClass();
            items.setData1("Dow Jones");
            items.setData2(our_table1.children().get(2).child(1).text());
            items.setData3(our_table1.children().get(2).child(2).text());
            items.setData4(our_table1.children().get(2).child(3).text());
            arrayList.add(items);
            /*
            doc2 = Jsoup.connect("https://www.investing.com/indices/eu-stoxx50").get();
            Elements tables2 = doc2.getElementsByTag("span");
            items = new ListItemClass();
            items.setData1("Euro Stoxx");
            items.setData2(tables2.get(70).text());
            items.setData3(tables2.get(71).text());
            items.setData4(tables2.get(72).text());
            arrayList.add(items);

             */
            Elements tables3 = doc1.getElementsByTag("span");
            items = new ListItemClass();
            items.setData1("MOEX");
            items.setData2(tables3.get(70).text());
            items.setData3(tables3.get(71).text());
            items.setData4(tables3.get(72).text());
            arrayList.add(items);
            Element our_table4 = tables1.get(6);
            items = new ListItemClass();
            items.setData1("Brent Oil");
            items.setData2(our_table4.children().get(1).child(1).text());
            items.setData3(our_table4.children().get(1).child(2).text());
            items.setData4(our_table4.children().get(1).child(3).text());
            arrayList.add(items);
            items = new ListItemClass();
            items.setData1("Natural Gas");
            items.setData2(our_table4.children().get(2).child(1).text());
            items.setData3(our_table4.children().get(2).child(2).text());
            items.setData4(our_table4.children().get(2).child(3).text());
            arrayList.add(items);
            items = new ListItemClass();
            items.setData1("Gold");
            items.setData2(our_table4.children().get(3).child(1).text());
            items.setData3(our_table4.children().get(3).child(2).text());
            items.setData4(our_table4.children().get(3).child(3).text());
            arrayList.add(items);
            items = new ListItemClass();
            items.setData1("Silver");
            items.setData2(our_table4.children().get(4).child(1).text());
            items.setData3(our_table4.children().get(4).child(2).text());
            items.setData4(our_table4.children().get(4).child(3).text());
            arrayList.add(items);
            items = new ListItemClass();
            items.setData1("Copper");
            items.setData2(our_table4.children().get(5).child(1).text());
            items.setData3(our_table4.children().get(5).child(2).text());
            items.setData4(our_table4.children().get(5).child(3).text());
            arrayList.add(items);
            doc3 = Jsoup.connect("https://www.investing.com/crypto/bitcoin/btc-usd").get();
            Elements tables5 = doc3.getElementsByTag("span");
            items = new ListItemClass();
            items.setData1("BTC/USD");
            items.setData2(tables5.get(70).text());
            items.setData3(tables5.get(71).text());
            items.setData4(tables5.get(72).text());
            arrayList.add(items);
            doc4 = Jsoup.connect("https://www.investing.com/crypto/ethereum/eth-usd").get();
            Elements tables6 = doc4.getElementsByTag("span");
            items = new ListItemClass();
            items.setData1("ETH/USD");
            items.setData2(tables6.get(70).text());
            items.setData3(tables6.get(71).text());
            items.setData4(tables6.get(72).text());
            arrayList.add(items);
            /*
            doc1 = Jsoup.connect("https://www.investing.com/indices/us-spx-500").get();
            Elements tables1 = doc1.getElementsByTag("span");
            ListItemClass items = new ListItemClass();
            items.setData1("S&P 500");
            items.setData2(tables1.get(70).text());
            items.setData3(tables1.get(71).text());
            items.setData4(tables1.get(72).text());
            arrayList.add(items);
            doc2 = Jsoup.connect("https://www.investing.com/indices/nasdaq-composite").get();
            Elements tables2 = doc2.getElementsByTag("span");
            items = new ListItemClass();
            items.setData1("NASDAQ");
            items.setData2(tables2.get(70).text());
            items.setData3(tables2.get(71).text());
            items.setData4(tables2.get(72).text());
            arrayList.add(items);
            doc3 = Jsoup.connect("https://www.investing.com/indices/us-30").get();
            Elements tables3 = doc3.getElementsByTag("span");
            items = new ListItemClass();
            items.setData1("Dow Jones");
            items.setData2(tables3.get(70).text());
            items.setData3(tables3.get(71).text());
            items.setData4(tables3.get(72).text());
            arrayList.add(items);
            doc4 = Jsoup.connect("https://www.investing.com/indices/eu-stoxx50").get();
            Elements tables4 = doc4.getElementsByTag("span");
            items = new ListItemClass();
            items.setData1("Euro Stoxx");
            items.setData2(tables4.get(70).text());
            items.setData3(tables4.get(71).text());
            items.setData4(tables4.get(72).text());
            arrayList.add(items);
            doc5 = Jsoup.connect("https://www.investing.com/indices/mcx").get();
            Elements tables5 = doc5.getElementsByTag("span");
            items = new ListItemClass();
            items.setData1("MOEX");
            items.setData2(tables5.get(70).text());
            items.setData3(tables5.get(71).text());
            items.setData4(tables5.get(72).text());
            arrayList.add(items);
            doc6 = Jsoup.connect("https://www.investing.com/commodities/brent-oil").get();
            Elements tables6 = doc6.getElementsByTag("span");
            items = new ListItemClass();
            items.setData1("Brent Oil");
            items.setData2(tables6.get(70).text());
            items.setData3(tables6.get(71).text());
            items.setData4(tables6.get(72).text());
            arrayList.add(items);
            doc7 = Jsoup.connect("https://www.investing.com/commodities/natural-gas").get();
            Elements tables7 = doc7.getElementsByTag("span");
            items = new ListItemClass();
            items.setData1("Natural Gas");
            items.setData2(tables7.get(70).text());
            items.setData3(tables7.get(71).text());
            items.setData4(tables7.get(72).text());
            arrayList.add(items);
            doc8 = Jsoup.connect("https://www.investing.com/commodities/gold").get();
            Elements tables8 = doc8.getElementsByTag("span");
            items = new ListItemClass();
            items.setData1("Gold");
            items.setData2(tables8.get(70).text());
            items.setData3(tables8.get(71).text());
            items.setData4(tables8.get(72).text());
            arrayList.add(items);
            doc9 = Jsoup.connect("https://www.investing.com/commodities/silver").get();
            Elements tables9 = doc9.getElementsByTag("span");
            items = new ListItemClass();
            items.setData1("Silver");
            items.setData2(tables9.get(70).text());
            items.setData3(tables9.get(71).text());
            items.setData4(tables9.get(72).text());
            arrayList.add(items);
            doc10 = Jsoup.connect("https://www.investing.com/commodities/copper").get();
            Elements tables10 = doc10.getElementsByTag("span");
            items = new ListItemClass();
            items.setData1("Copper");
            items.setData2(tables10.get(70).text());
            items.setData3(tables10.get(71).text());
            items.setData4(tables10.get(72).text());
            arrayList.add(items);
            doc11 = Jsoup.connect("https://www.investing.com/crypto/bitcoin/btc-usd").get();
            Elements tables11 = doc11.getElementsByTag("span");
            items = new ListItemClass();
            items.setData1("BTC/USD");
            items.setData2(tables11.get(70).text());
            items.setData3(tables11.get(71).text());
            items.setData4(tables11.get(72).text());
            arrayList.add(items);
            doc12 = Jsoup.connect("https://www.investing.com/crypto/ethereum/eth-usd").get();
            Elements tables12 = doc12.getElementsByTag("span");
            items = new ListItemClass();
            items.setData1("ETH/USD");
            items.setData2(tables12.get(70).text());
            items.setData3(tables12.get(71).text());
            items.setData4(tables12.get(72).text());
            arrayList.add(items);
            */
            getActivity().runOnUiThread(new Runnable() { // адаптер нельзя трогать с второстепенного потока
                @Override
                public void run() {
                    adapter.notifyDataSetChanged(); // обновить информацию
                }
            });
        } catch (IOException e){
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