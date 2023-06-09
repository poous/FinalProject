package com.example.finalproject;

import static android.content.ContentValues.TAG;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.LinearLayout;

import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link SearchFragment1#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SearchFragment1 extends Fragment {
    //    SettingList settingList;
    private List<String> list;          // 데이터를 넣은 리스트변수
    Context context;


    private List<String> jsonId;          // 검색을 보여줄 리스트변수
    private List<String> jsonname;
    private List<String> jsonintroduction;
    private List<String> jsontime;
    private List<String> jsoncalorie;
    private List<String> jsoncapacity;
    private List<String> jsondifficulty;
    private List<String> jsonimage_link;
    private GridView gridView;
    private EditText editSearch;        // 검색어를 입력할 Input 창
    private SearchAdapter adapter;      // 리스트뷰에 연결할 아답터
    private SearchTagAdapter tagAdapter;
    private ArrayList<String> arraylist;//전체리스트 저장
    private ArrayList<String> testDataSet;
    private SearchView searchView;//검색창

    private ImageButton imageBut;//태그 검색 버튼
    private boolean tagbool = false;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public SearchFragment1() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment SearchFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static SearchFragment1 newInstance(String param1, String param2) {
        SearchFragment1 fragment = new SearchFragment1();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View frag_view = inflater.inflate(R.layout.fragment_search, container, false);
        //editSearch = (EditText) frag_view.findViewById(R.id.editSearch);
        //listView = (ListView) frag_view.findViewById(R.id.listView);

        Bundle bundle = getArguments();
        String access_token = bundle.getString("access_token");

        jsonId = new ArrayList<>();
        jsonname = new ArrayList<>();
        jsonintroduction = new ArrayList<>();
        jsontime = new ArrayList<>();
        jsoncalorie = new ArrayList<>();
        jsoncapacity = new ArrayList<>();
        jsondifficulty = new ArrayList<>();
        jsonimage_link = new ArrayList<>();

        list = new ArrayList<String>();
        imageBut = frag_view.findViewById(R.id.imagebut);

        testDataSet = new ArrayList<>();
        RecyclerView recyclerView = frag_view.findViewById(R.id.listView);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(frag_view.getContext(), RecyclerView.HORIZONTAL, false);
        recyclerView.setLayoutManager(linearLayoutManager);  // LayoutManager 설정
        CustomAdapter customAdapter = new CustomAdapter(testDataSet);
        recyclerView.setAdapter(customAdapter); // 어댑터 설정

        gridView = (GridView) frag_view.findViewById(R.id.gridView);
        searchView = frag_view.findViewById(R.id.search);
        context = container.getContext();


        settingList();
        arraylist = new ArrayList<String>();
        arraylist.addAll(list);
        adapter = new SearchAdapter(list, container.getContext());
        gridView.setAdapter(adapter);


        searchView.setOnSearchClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.e("search", "click");
                adapter = new SearchAdapter(list, container.getContext());
                gridView.setAdapter(adapter);
            }
        });

        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                                Log.d(TAG, "여기야 : "+l_id.get(position));
//                                Intent intent =new Intent(getActivity(),ProductActivity.class);
//                                intent.putExtra("id",l_id.get(position));
//                                startActivity(intent);
                if (tagbool == false) {
                    testDataSet.add(list.get(position));
                    customAdapter.notifyDataSetChanged();
                } else {
                    //                                Log.d(TAG, "여기야 : "+l_id.get(position));
                    Intent intent = new Intent(getActivity(), RecipeActivity.class);
                    intent.putExtra("id", jsonId.get(position));
                    intent.putExtra("recipeimage", jsonimage_link.get(position));
                    intent.putExtra("recipename", jsonname.get(position));
                    intent.putExtra("recipetime", jsontime.get(position));
                    intent.putExtra("recipedifficulty", jsondifficulty.get(position));
                    intent.putExtra("recipecapacity", jsoncapacity.get(position));
                    intent.putExtra("access_token", access_token);
                    startActivity(intent);
                }
            }
        });
        customAdapter.setOnItemClickListener(new CustomAdapter.OnItemClickListener() {
            @Override
            public void onItemClicked(int position, String data) {
                Log.e("position", String.valueOf(position));
                testDataSet.remove(position);
                customAdapter.notifyDataSetChanged();
            }
        });
        //==========================================================
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {


            @Override
            public boolean onQueryTextSubmit(String query) {


                Log.d(TAG, "rrrrrrrrrrrrrrrrrrrrrr");
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                adapter = new SearchAdapter(list, container.getContext());
                gridView.setAdapter(adapter);
                tagbool = false;
                search(newText);
                return false;
            }
        });
        imageBut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                jsonRequest(container,frag_view,access_token);

            }
        });

        return frag_view;


    }

    // 검색을 수행하는 메소드
    public void search(String charText) {

        // 문자 입력시마다 리스트를 지우고 새로 뿌려준다.
        list.clear();

        // 문자 입력이 없을때는 모든 데이터를 보여준다.
        if (charText.length() == 0) {
            list.addAll(arraylist);
        }
        // 문자 입력을 할때..
        else {
//            // 리스트의 모든 데이터를 검색한다.
            for (int i = 0; i < arraylist.size(); i++) {
//                // arraylist의 모든 데이터에 입력받은 단어(charText)가 포함되어 있으면 true를 반환한다.
                if (arraylist.get(i).toLowerCase().contains(charText)) {
//                    // 검색된 데이터를 리스트에 추가한다.

                    list.add(arraylist.get(i));
                }

            }
        }
        // 리스트 데이터가 변경되었으므로 아답터를 갱신하여 검색된 데이터를 화면에 보여준다.
        adapter.notifyDataSetChanged();
    }

    // 검색에 사용될 데이터를 리스트에 추가한다.
    private void settingList() {
        SearchRepository dao = SearchRepository.getInstance();
        list.addAll(dao.getAllProducts());

    }

    private void jsonRequest(ViewGroup container,View frag_view,String access_token){
        Response.Listener<String> searchTagResponseListener = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    jsonId.clear();
                    jsonname.clear();
                    jsonintroduction.clear();
                    jsontime.clear();
                    jsoncalorie.clear();
                    jsoncapacity.clear();
                    jsondifficulty.clear();
                    jsonimage_link.clear();
                    JSONObject jsonObject = new JSONObject(response);

                    Log.e("Test123", jsonObject.toString());
                    JSONArray data_json = jsonObject.getJSONArray("data");
                    //Log.e("Test123",data_json.toString());
                    for (int i = 0; i < data_json.length(); i++) {
                        JSONObject jsonObject1 = data_json.getJSONObject(i);
                        // jsonObject1=data_json.getJSONObject(i);
                        //Log.e("Test123",jsonObject1.toString());
                        jsonId.add(jsonObject1.getString("id"));
                        jsonname.add(jsonObject1.getString("name"));
                        jsonintroduction.add(jsonObject1.getString("introduction"));
                        jsontime.add(jsonObject1.getString("time"));
                        jsoncalorie.add(jsonObject1.getString("calorie"));
                        jsoncapacity.add(jsonObject1.getString("capacity"));
                        jsondifficulty.add(jsonObject1.getString("difficulty"));
                        jsonimage_link.add(jsonObject1.getString("image_link"));

                        //Log.e("name",data_json.getString(i));
                    }

                    tagAdapter = new SearchTagAdapter(jsonname, jsonintroduction, jsontime, jsoncalorie, jsoncapacity, jsondifficulty, jsonimage_link, container.getContext());
                    gridView.setAdapter(tagAdapter);

                    tagbool = true;
                    //Log.d("token",access_token);

                } catch (JSONException e) {
                    e.printStackTrace();
                    Log.e("err", "Searchtag json err");
                    jsonRequest(container,frag_view,access_token);
                }
            }
        };

        String dataset = "";

        for (int i = 0; i < testDataSet.size(); i++) {
            if (i > 0 && i < testDataSet.size()) {
                dataset += "&";
            }
            dataset += "ingredients[]=" + testDataSet.get(i);
        }
        Log.e("tagck", dataset);
        SearchTagRequest searchTagRequest = new SearchTagRequest(access_token, searchTagResponseListener, dataset);
        RequestQueue searchqueue = Volley.newRequestQueue(frag_view.getContext());
        searchqueue.add(searchTagRequest);
    }
}