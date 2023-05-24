package com.mte.fitnessapp.ui.home.profile

import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import com.google.firebase.Timestamp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import com.mte.fitnessapp.R
import com.mte.fitnessapp.databinding.FragmentProfileBinding
import com.squareup.picasso.Picasso
import java.util.UUID

class ProfileFragment : Fragment() {
    private var _binding : FragmentProfileBinding? = null
    private val binding get() = _binding!!

    val storageRef = Firebase.storage.reference
    val db= Firebase.firestore
    private lateinit var auth: FirebaseAuth
    var databaseReference: DatabaseReference?=null
    var database: FirebaseDatabase?=null


    lateinit var imageUri: Uri
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        auth= FirebaseAuth.getInstance()
        database=FirebaseDatabase.getInstance()
        databaseReference=database?.reference!!.child("profile")
        var userReference = databaseReference?.child(auth.uid!!)
        var profilPhoto=""
        var userName=""
        userReference?.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                profilPhoto = (snapshot.child("profilPhoto").value.toString())
                userName = (snapshot.child("username").value.toString())
                binding.profileName.text=userName
                if (profilPhoto!="null"){
                    Log.e("id:",profilPhoto)
                    Picasso.get().load(profilPhoto).into(binding.profilPhoto)
                }

            }

            override fun onCancelled(error: DatabaseError) {

            }
        })
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding=FragmentProfileBinding.inflate(inflater,container,false)
        return binding.root
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        loadFragment(ProfilePhotosFragment())
        val resim = registerForActivityResult(ActivityResultContracts.PickVisualMedia()) { uri ->
            if (uri != null) {
                imageUri = Uri.parse(uri.toString())
                val uuid = UUID.randomUUID()
                val imageName = "${uuid}.jpg"
                val imageRef = storageRef.child("images/${auth.uid}/profilphoto/${imageName}")
                val uploadTask = imageRef.putFile(imageUri)
                uploadTask.addOnSuccessListener {
                    val resimLink = imageRef
                    var downloadPhoto = ""
                    resimLink.downloadUrl.addOnSuccessListener {
                        downloadPhoto = it.toString()
                        val currentUser = auth.currentUser
                        val currentUserDb = databaseReference?.child((currentUser?.uid!!))
                        currentUserDb?.child("profilPhoto")?.setValue(downloadPhoto)
                        binding.profilPhoto.setImageURI(imageUri)
                    }


                }

            }
        }
        binding.profilPhoto.setOnClickListener {
            resim.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))

        }

        binding.profileBarNavigation.setOnItemSelectedListener {
            when (it.itemId) {
                R.id.photospage -> {
                    loadFragment(ProfilePhotosFragment())
                    true
                }
                R.id.settingspage -> {
                    loadFragment(SettingsFragment())
                    true
                }
                else -> {
                    false
                }
            }
        }

    }

    private  fun loadFragment(fragment: Fragment){
        val transaction = childFragmentManager.beginTransaction()
        transaction.replace(R.id.profileContainer,fragment)
        transaction.addToBackStack(null)
        transaction.commit()
    }


}
