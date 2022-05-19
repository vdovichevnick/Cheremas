package com.example.t2.ui.porfolio;

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
//import com.example.t2.databinding.FragmentNotificationsBinding;
import com.example.t2.ui.CustomArrayAdapter;
import com.example.t2.ui.ListItemClass;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

public class PorfolioFragment extends Fragment {
    private Document doc;
    private Thread secTread;
    private Runnable runnable;
    private ListView listView;
    private CustomArrayAdapter adapter;
    private List<ListItemClass> arrayList;
    private EditText userText;
    private Button butText;
    private TextView textStock1;
    private TextView textView1;
    private TextView textView2;
    private TextView textView3;
    private TextView textView4;
    private TextView textView5;
    private TextView textView6;


    //private FragmentNotificationsBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        PorfolioViewModel porfolioViewModel =
                new ViewModelProvider(this).get(PorfolioViewModel.class);

        //binding = FragmentNotificationsBinding.inflate(inflater, container, false);
        View v = inflater.inflate(R.layout.fragment_portfolio, container, false);
        userText = v.findViewById(R.id.userText);
        butText = v.findViewById(R.id.butText);
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
        double numbers[] = new double [7];
        butText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (userText.getText().toString().trim().equals("")) {
                    Toast.makeText(getActivity().getApplicationContext(), R.string.noUserInput, Toast.LENGTH_LONG).show();
                } else {
                    //textStock1.setText("Stock searching...");
                    String ticker = userText.getText().toString();
                    textStock1.setText("Stock searching...");
                    init(ticker, v, numbers);

                }
            }
        });

        //dashboardViewModel.getText().observe(getViewLifecycleOwner(), textView::setText);
        return v;
    }

    private void init(String s, View v, double numbers[]) {


        runnable = new Runnable() {
            @Override
            public void run() {
                getWeb(s,numbers);
            }
        };
        secTread = new Thread(runnable);
        secTread.start();

    }
    private void getWeb(String s, double numbers[]) {
        try {
            doc = Jsoup.connect("https://finviz.com/quote.ashx?t=" + s).get();
            Elements tables = doc.getElementsByTag("tbody");
            //Element our_table = tables.get(8); //таблица с показателями
            ListItemClass items = new ListItemClass();
            String sector = tables.get(6).children().get(2).children().get(0).children().get(0).text();
            items.setData1(s.toUpperCase());
            items.setData2(sector);
            items.setData3(tables.get(8).children().get(10).children().get(11).text());
            items.setData4(tables.get(8).children().get(11).children().get(11).text());
            arrayList.add(items); //закинуть в адаптер
            getActivity().runOnUiThread(new Runnable() { // адаптер нельзя трогать с второстепенного потока
                @Override
                public void run() {
                    textStock1.setText("Added to your porfolio!"); // обновить информацию
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
                    DecimalFormat df = new DecimalFormat("##.#");
                    textView1.setText("Technology: " + (df .format(numbers[1]/numbers[0]*100))+"%");
                    textView2.setText("Healthcare: " + (df .format(numbers[2]/numbers[0]*100))+"%");
                    textView3.setText("Financial: " + (df .format(numbers[3]/numbers[0]*100))+"%");
                    textView4.setText("Energy: " + (df .format(numbers[4]/numbers[0]*100))+"%");
                    textView5.setText("Industrials: " + (df .format(numbers[5]/numbers[0]*100))+"%");
                    textView6.setText("Other: " + (df .format(numbers[6]/numbers[0]*100))+"%");
                    /*
                    textView1.setText("Technology: " + (String.format("%.1f",numbers[1]/numbers[0]*100))+"%");
                    textView2.setText("Healthcare: " + (String.format("%.1f",numbers[2]/numbers[0]*100))+"%");
                    textView3.setText("Financial: " + (String.format("%.1f",numbers[3]/numbers[0]*100))+"%");
                    textView4.setText("Energy: " + (String.format("%.1f",numbers[4]/numbers[0]*100))+"%");
                    textView5.setText("Industrials: " + (String.format("%.1f",numbers[5]/numbers[0]*100))+"%");
                    textView6.setText("Other: " + (String.format("%.1f",numbers[6]/numbers[0]*100))+"%");
                     */
                }
            });
                    // text().substring(0,7) - вырезать по индексу
            getActivity().runOnUiThread(new Runnable() { // адаптер нельзя трогать с второстепенного потока
                @Override
                public void run() {
                    adapter.notifyDataSetChanged(); // обновить информацию
                }
            });
        } catch (IOException e) {
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
