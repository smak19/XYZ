package com.example.xyz;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;

public class Adapter extends RecyclerView.Adapter<Adapter.MyViewHolder> {
    ArrayList<CharityModel> arrayList;

    Context context;

    public Adapter(ArrayList<CharityModel> arrayList, Context context ) {
        this.arrayList= arrayList;
        this.context = context;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = (View) LayoutInflater.from(context).inflate(R.layout.charityitem_list, parent, false);


        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        holder.name.setText("Name : "+arrayList.get(position).getName());
        holder.orgname2 = arrayList.get(position).getName();
        holder.address.setText("Address : " +arrayList.get(position).getAddress());
        holder.bg = arrayList.get(position).getBloodgroup();
        holder.donate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(arrayList.get(position).getCategory().equals("Blood")) {
                    Intent intent = new Intent(context, BloodCategoryList.class);
                    intent.putExtra("Blood Group", holder.bg);
                    intent.putExtra("Org Name", holder.orgname2);
                    intent.putExtra("Category", arrayList.get(position).getCategory());
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    Log.e("bg", "" + holder.bg);
                    context.startActivity(intent);
                }

                if (arrayList.get(position).getCategory().equals("Clothes")){
                    Intent intent = new Intent(context , ClothingCategoryList.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    intent.putExtra("Org Name", holder.orgname2);
                    intent.putExtra("Category", arrayList.get(position).getCategory());
                    context.startActivity(intent);
                }

                if (arrayList.get(position).getCategory().equals("Grocery")){
                    Intent intent = new Intent(context ,GroceryCategoryList.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    intent.putExtra("Org Name", holder.orgname2);
                    intent.putExtra("Category", arrayList.get(position).getCategory());
                    context.startActivity(intent);
                }

                if (arrayList.get(position).getCategory().equals("Food")){
                    Intent intent = new Intent(context ,FoodCategoryList.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    intent.putExtra("Org Name", holder.orgname2);
                    intent.putExtra("Category", arrayList.get(position).getCategory());
                    context.startActivity(intent);
                }

                if(arrayList.get(position).getCategory().equals("Books")){
                    Intent intent = new Intent(context , BooksCategoryList.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    intent.putExtra("Org Name", holder.orgname2);
                    intent.putExtra("Category", arrayList.get(position).getCategory());
                    context.startActivity(intent);
                }


            }
        });


    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{
        TextView name, address ;
        Button donate;
        String bg , orgname2;


        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.orgnameitem);
            address = itemView.findViewById(R.id.orgaddress);
            donate = itemView.findViewById(R.id.Donate);

        }
    }
}
