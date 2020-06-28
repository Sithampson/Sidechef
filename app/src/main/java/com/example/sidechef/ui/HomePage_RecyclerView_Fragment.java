package com.example.sidechef.ui;

import android.database.Cursor;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.sidechef.Adapter.RecyclerViewAdapter;
import com.example.sidechef.Data.DataBaseHelper;
import com.example.sidechef.Model.Food;
import com.example.sidechef.Model.sql_query;
import com.example.sidechef.R;

import java.util.ArrayList;

public class HomePage_RecyclerView_Fragment extends Fragment {
    ArrayList<Food> list;
    String query;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.homepage_recyclerview_fragment, container, false);
        RecyclerView recyclerView = root.findViewById(R.id.recyclerView);
        int numofcolumns = 2;
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new GridLayoutManager(getActivity(), numofcolumns));

        DataBaseHelper db = new DataBaseHelper(getActivity());
        list = new ArrayList<>();

        Cursor cursor = db.getdata("SELECT * from Recipe ORDER by " + DataBaseHelper.reccol_9 + " DESC");
        list.clear();
        while (cursor.moveToNext()) {
            int id = cursor.getInt(0);
            String name = cursor.getString(1);
            byte[] image = cursor.getBlob(7);

            list.add(new Food(id, name, image));
        }

        RecyclerViewAdapter recyclerViewAdapter = new RecyclerViewAdapter(getActivity(), list);
        recyclerView.setAdapter(recyclerViewAdapter);
        return root;
    }

    public void get_query(sql_query sql_query){
        query = sql_query.getSqlquery();
    }

}
