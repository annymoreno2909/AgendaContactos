package com.example.agenda

import android.app.appsearch.StorageInfo
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
//import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.IdRes
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import com.example.agenda.Classes.Contact
import com.example.agenda.Classes.Response
import com.example.agenda.SQLite.ContactsDBHelper
import com.example.agenda.databinding.FragmentCreateContactBinding
import com.google.gson.Gson

/**
 * A simple [Fragment] subclass as the second destination in the navigation.
 */
class InfoContactFragment : Fragment() {

    private var _binding: FragmentCreateContactBinding? = null
    var contacto: Contact= Contact()
    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentCreateContactBinding.inflate(inflater, container, false)
        return binding.root

    }
    fun showInfo(){
        if(contacto.id>0){
            binding.edTxtwName.setText(contacto.name.toString())
            var strEdad:String=""
            if(contacto.age>0){strEdad=contacto.age.toString()}
            binding.edTxtwAge.setText(strEdad)
            binding.edTxtwPhone.setText(contacto.phone)
            binding.edTxtwPLastName.setText(contacto.p_last_name)
            binding.edTxtwMLastName.setText(contacto.m_last_name)
            if(contacto.sex.equals("F")){binding.rdGroupGender.check(R.id.rdBtnFemale)}
            else{binding.rdGroupGender.check(R.id.rdBtnMale)}

        }
        else{
            binding.btnDelete.visibility=View.GONE
            
        }

    }

    fun NavController.safelyNavigate(@IdRes resId: Int, args: Bundle? = null) {
        try { navigate(resId, args) }
        catch (e: Exception) {  }
}
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        /*TODO: seleccion de imagen*/
      /*  val pickVisualMedia=registerForActivityResult(
            ActivityResultContracts.PickVisualMedia( )
        ){uri->
            if(uri!=null){
                contacto.photo=uri.toString()
            }
            else{contacto.photo=""}

        }*/
        var contactsDBHelper: ContactsDBHelper= ContactsDBHelper(context,null)
        var response:Response
        showInfo();
        var strContact:String=""
        try {
            var gson: Gson =Gson()
            strContact= arguments?.get("contacto") as String
            contacto=gson.fromJson(strContact, Contact::class.java)
        }catch(e:java.lang.Exception){}
    /*TODO: lanzar evento de busque de imagen*/
       /* binding.imgBtnPhoto.setOnClickListener(
            View.OnClickListener { v-> pickVisualMedia.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)) }
        )
        //regresar a lista
        binding.btnReturnToList.setOnClickListener { view->
            findNavController().navigate(R.id.Info_To_Contact_List)

        }*/
        //actualizacion/creacion
        binding.btnSaveContact.setOnClickListener {view ->
            var strName:String =binding.edTxtwName.getText().toString()
            var strPhone:String=binding.edTxtwPhone.text.toString()
            var strPLastName:String=binding.edTxtwPLastName.text.toString();
            var strMLastName:String= binding.edTxtwMLastName.text.toString()
            var intAge:Int= 0
            try {
                intAge=binding.edTxtwAge.text.toString().toInt();
            } catch (e: Exception) {                // handler
            }
                        contacto.sex=""
            var selectedId = binding.rdGroupGender.checkedRadioButtonId
            when (selectedId) {
                R.id.rdBtnFemale->
                    contacto.sex="F"
                R.id.rdBtnMale->
                    contacto.sex="M"


            }
            if(strName.length>0&&strPhone.length>=10&&contacto.sex.length>0){
                contacto.name=strName;
                contacto.phone=strPhone
                contacto.age=intAge
                contacto.m_last_name=strMLastName
                contacto.p_last_name=strPLastName

                //
                if(contacto.id>0){
                    //edicion de contacto
                    response=contactsDBHelper.editContact(contacto)
                    if(response.code.equals(0)){
                        Toast.makeText(context,"Contacto actualizado",Toast.LENGTH_LONG).show()
                        Thread.sleep((Toast.LENGTH_LONG*1000).toLong())
                        //
                    }
                    else{Toast.makeText(context,response.msg,Toast.LENGTH_LONG).show()

                    }
                }
                else {
                    //creacion
                    if (!contactsDBHelper.registroConTelefonoExiste(contacto)){
                        response = contactsDBHelper.createContact(contacto)
                    if (response.code.equals(0)) {
                        contacto=response.obj as Contact;
                        Toast.makeText(context, "Contacto registrado con éxito", Toast.LENGTH_LONG)
                            .show()

                        Thread.sleep((Toast.LENGTH_LONG * 1000).toLong())
                        binding.btnDelete.visibility = View.VISIBLE
                        //
                    } else {
                        Toast.makeText(context, response.msg, Toast.LENGTH_LONG).show()

                    }
                }else{Toast.makeText(context,"Ya existe otro usuario con este número de telefono", Toast.LENGTH_LONG).show()}
                    }

            }
            else{
                if (strName.length==0) {
                    Toast.makeText(context, "Captura nombre", Toast.LENGTH_LONG).show()
                }
                else if (strPhone.length<10){
                    Toast.makeText(context, "El campo de telefono debe tener al menos 10 digitos", Toast.LENGTH_LONG).show();
                }
                else if (contacto.sex.length==0){
                    Toast.makeText(context, "El campode género es obligatorio", Toast.LENGTH_LONG).show();
                }
            }

        }
        //eliminacion
        binding.btnDelete.setOnClickListener(View.OnClickListener {
            val response : Response=     contactsDBHelper.deleteContact(contacto)
            if(response.code.equals(0)){
                Toast.makeText(context,"Contacto  eliminado",Toast.LENGTH_LONG).show()
                Thread.sleep(((Toast.LENGTH_LONG*1000).toLong()))
                findNavController().navigate(R.id.Info_To_Contact_List)
            }
        })
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


}
