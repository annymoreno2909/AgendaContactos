package com.example.agenda.Adapters;

import android.content.Context;
import android.os.Bundle;
import android.renderscript.ScriptGroup;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.agenda.Classes.Contact;
import com.example.agenda.FirstFragment;
import com.example.agenda.R;
import com.example.agenda.SQLite.ContactsDBHelper;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.gson.Gson;

import java.util.ArrayList;

public class ContactAdapter  extends RecyclerView.Adapter<ContactAdapter.MyViewHolder> implements Filterable, RecyclerView.OnChildAttachStateChangeListener  {
    private Context context;
    ArrayList<Contact> arrayContacts= new ArrayList<>();

    public ContactAdapter(Context context, ArrayList<Contact> arrayContacts) {
        this.context = context;
        this.arrayContacts = arrayContacts;
    }


    @Override
    public Filter getFilter() {
        return null;
    }

    @NonNull
    @Override
    public ContactAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_contact, parent, false);
        // creo el grupo de vistas
        ContactAdapter.MyViewHolder vh = new ContactAdapter.MyViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull ContactAdapter.MyViewHolder holder, int position) {
        final Contact contact=arrayContacts.get(position);
        holder.txtVwName.setText(""+contact.name);
        ContactsDBHelper contactsDBHelper= new ContactsDBHelper(context,null);


    }

    @Override
    public int getItemCount() {
        return arrayContacts.size();
    }

    @Override
    public void onChildViewAttachedToWindow(@NonNull View view) {

    }

    @Override
    public void onChildViewDetachedFromWindow(@NonNull View view) {

    }
    public  static class MyViewHolder  extends RecyclerView.ViewHolder{
        public TextView txtVwName;
        public ImageView imgVw;
        public MyViewHolder(View itemView) {
            super(itemView);
            txtVwName=itemView.findViewById(R.id.txtVwName);
            itemView.setTag(this);
            itemView.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {

                }
            });

        }

    }
}
