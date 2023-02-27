package com.lpirro.transaction.presentation.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.lpirro.domain.model.Command
import com.lpirro.transactionscreen.databinding.ItemCommandBinding

class CommandAdapter : ListAdapter<Command, CommandAdapter.CommandViewHolder>(CommandDiffCallback) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CommandViewHolder {
        val binding = ItemCommandBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return CommandViewHolder(binding)
    }

    override fun onBindViewHolder(holder: CommandViewHolder, position: Int) {
        when (val command = getItem(position)) {
            is Command.Input -> holder.binding.commandTextView.text = "> $command"
            is Command.Error -> holder.binding.commandTextView.text = command.message
            is Command.Output -> holder.binding.commandTextView.text = command.output
        }
    }

    inner class CommandViewHolder(val binding: ItemCommandBinding) :
        RecyclerView.ViewHolder(binding.root)
}
