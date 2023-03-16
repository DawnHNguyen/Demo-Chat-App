package com.example.authentication.presentation

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.appcompat.content.res.AppCompatResources
import androidx.recyclerview.widget.RecyclerView
import com.example.authentication.databinding.SsoItemBinding
import com.example.authentication.domain.entity.SSOEntity

class SSORecyclerViewAdapter(val data: List<SSOEntity>, val onClickSSO: OnClickSSO): RecyclerView.Adapter<SSORecyclerViewAdapter.SSOViewHolder>() {
    class SSOViewHolder (val binding: SsoItemBinding): RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SSOViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = SsoItemBinding.inflate(layoutInflater, parent, false)
        return SSOViewHolder(binding)
    }

    override fun onBindViewHolder(holder: SSOViewHolder, position: Int) {
        val item = data[position]
        holder.binding.platformName = item.platformName
        holder.binding.platformLogo = AppCompatResources.getDrawable(holder.binding.root.context, item.icon)
        holder.binding.parentViewItemSSo.setOnClickListener { onClickSSO.loginSSO(item.platform) }
    }

    override fun getItemCount(): Int = data.size
}