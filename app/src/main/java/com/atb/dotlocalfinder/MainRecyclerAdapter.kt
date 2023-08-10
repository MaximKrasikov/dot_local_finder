/*
 *    Copyright 2020- Network Revolution Inc.
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 *
 */

package com.atb.dotlocalfinder

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.atb.dotlocalfinder.databinding.MainRecyclerItemBinding

class MainRecyclerAdapter(
    private val listener: OnCardClickListener
) : RecyclerView.Adapter<MainRecyclerAdapter.ViewHolder>() {
    var dataList = emptyList<MainDataClass>()

    interface OnCardClickListener {
        fun onCardClicked(mainDataClass: MainDataClass)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = ViewHolder(
        MainRecyclerItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
    )

    override fun getItemCount() = dataList.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.binding.also {
            it.itemData = dataList[position]
            it.itemClickListener = listener
            it.executePendingBindings()
        }
    }

    class ViewHolder(val binding: MainRecyclerItemBinding) : RecyclerView.ViewHolder(binding.root)
}