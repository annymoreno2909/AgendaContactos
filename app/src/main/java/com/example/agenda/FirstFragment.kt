package com.example.agenda

import android.app.Activity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.agenda.Adapters.ContactAdapter
import com.example.agenda.Classes.Contact
import com.example.agenda.Classes.Response
import com.example.agenda.SQLite.ContactsDBHelper
import com.example.agenda.databinding.FragmentContactListBinding
import java.text.FieldPosition
import com.google.gson.Gson

/**
 * A simple [Fragment] subclass as the default destination in the navigation.
 */
class FirstFragment : Fragment() {

    //lista
    var contactAdapter:ContactAdapter?=null
    var linearLayoutManager: LinearLayoutManager? = null

    //cambio de fragments


    private var _binding: FragmentContactListBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentContactListBinding.inflate(inflater, container, false)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.fab.setOnClickListener { view->
            findNavController().navigate(R.id.action_FirstFragment_to_SecondFragment)

        }
        var contactsDBHelper: ContactsDBHelper= ContactsDBHelper(context,null)
        var response:Response=contactsDBHelper.allContacts;
        var contactos:ArrayList<Contact>
        if(response.code==0){
            contactos= response.obj as ArrayList<Contact>
            linearLayoutManager = LinearLayoutManager(context)
            binding.rcVwContacts.setLayoutManager(linearLayoutManager)
            contactAdapter = ContactAdapter(
                context,
                contactos
            )
            binding.rcVwContacts.adapter=contactAdapter
            /**GESTOS AL RECYCLER VIEW */
            val simpleCallback: ItemTouchHelper.SimpleCallback = object :
                ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.ANIMATION_TYPE_SWIPE_CANCEL) {
                //ItemTouchHelper.RIGHT | ItemTouchHelper.LEFT){
                override fun onMove(
                    recyclerView: RecyclerView,
                    viewHolder: RecyclerView.ViewHolder,
                    viewHolder1: RecyclerView.ViewHolder
                ): Boolean {

                setContactInfo(contactos.get(viewHolder.position))
                    return false
                }

                override fun onSwiped(viewHolder: RecyclerView.ViewHolder, i: Int) {
                    //Toast.makeText(ActivityMenu.context,"Swiped to Right",Toast.LENGTH_LONG).show();
                    setContactInfo(contactos.get(viewHolder.position)
                    )
                }
            }
            val myHelper = ItemTouchHelper(simpleCallback)
            myHelper.attachToRecyclerView(binding.rcVwContacts)
        }
        else{Toast.makeText(context,response.msg,Toast.LENGTH_LONG).show()}

    }
    fun setContactInfo(contact: Contact){
        var gson:Gson=Gson()
        val bundle = bundleOf("contact" to gson.toJson(contact) )
        findNavController().navigate(R.id.action_FirstFragment_to_SecondFragment, bundle)

        var fragmentManager:FragmentManager= parentFragmentManager
        var fragmentTransaction = fragmentManager!!.beginTransaction()
        var fragmentInfo: InfoContactFragment= InfoContactFragment()
        fragmentInfo.contacto= contact
        fragmentTransaction.replace(R.id.fragment_content_main, fragmentInfo)
        fragmentTransaction.addToBackStack(null)
        fragmentTransaction.commit()
    }
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}