package com.example.sidechef.ui.UserRecipes;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.sidechef.Model.UserApi;
import com.example.sidechef.Model.Food;
import com.example.sidechef.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class UserRecipesFragment extends Fragment {

    ArrayList<Food> list;
    RecyclerViewAdapter_UserRecipes recyclerViewAdapterUserRecipes;
    RecyclerView recyclerView;

    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private CollectionReference collectionReference = db.collection("Recipe");

    public UserRecipesFragment(){}

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_homepage_recyclerview, container, false);
        recyclerView = root.findViewById(R.id.RecyclerView_HomePage);

        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        list = new ArrayList<>();
        list.clear();

        return root;
    }

    @Override
    public void onStart() {
        super.onStart();
        collectionReference.whereEqualTo("userId", UserApi.getInstance().getUserId()).get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        if(!queryDocumentSnapshots.isEmpty()){
                            for(QueryDocumentSnapshot recipe : queryDocumentSnapshots){
                                Food food  = new Food();
                                food.setName(recipe.getString("recipeName"));
                                food.setImageUrl(recipe.getString("imageUrl"));
                                food.setId(recipe.getId());

                                list.add(food);
                            }

                            recyclerViewAdapterUserRecipes = new RecyclerViewAdapter_UserRecipes(getActivity(), list);
                            recyclerView.setAdapter(recyclerViewAdapterUserRecipes);
                            recyclerViewAdapterUserRecipes.notifyDataSetChanged();
                        }
                        else{
                            Toast.makeText(getActivity(), "There are no recipes", Toast.LENGTH_SHORT).show();

                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.d("RecyclerView_Home", e.toString());
                    }
                });
    }
}