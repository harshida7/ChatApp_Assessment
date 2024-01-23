package com.example.chatapp_assessment.Adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.example.chatapp_assessment.Model.RegisterUserModel
import com.example.chatapp_assessment.R
import com.google.android.material.card.MaterialCardView
import com.squareup.picasso.Picasso

class DashboardAdapter(var context: Context, var list: List<RegisterUserModel>): RecyclerView.Adapter<MyViewDashboard>()
{
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewDashboard {
        var  layoutInflater= LayoutInflater.from(parent.context)
        var view = layoutInflater.inflate(R.layout.design_dashboard,parent,false)
        return MyViewDashboard(view)

    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: MyViewDashboard, position: Int) {

        holder.txtUserName.setText(list.get(position).username)

    }
}

class MyViewDashboard(Itemview: View):RecyclerView.ViewHolder(Itemview) {

    var txtUserName: TextView = Itemview.findViewById(R.id.txtUsername)
    var txtUserData: TextView = Itemview.findViewById(R.id.txtUserData)
    var imageview: ImageView = Itemview.findViewById(R.id.imgUserProfile)

}
