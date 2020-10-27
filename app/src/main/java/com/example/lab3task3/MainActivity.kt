package com.example.lab3task3

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import name.ank.lab4.BibDatabase
import name.ank.lab4.Keys
import java.io.InputStreamReader
import java.util.*


class MainActivity : AppCompatActivity() {

    private lateinit var database: BibDatabase
    private val items: MutableList<Item> = ArrayList()
    private val adapter: RecyclerView.Adapter<*> = ItemAdapter(this.items)

    override fun onCreate(savedInstanceState: Bundle?) {
        initializeList()
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val recycler = findViewById<RecyclerView>(R.id.recycler)
        recycler.setHasFixedSize(true)
        recycler.layoutManager = LinearLayoutManager(this)
        recycler.adapter = adapter
        recycler.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                if ((recycler.layoutManager as LinearLayoutManager).findLastCompletelyVisibleItemPosition() == items.size - 1) reloadList()
            }
        })
    }

    private fun initializeList() {
        InputStreamReader(
            resources.openRawResource(
                resources.getIdentifier(
                    "articles",
                    "raw",
                    packageName
                )
            )
        ).use { reader ->
            this.database = BibDatabase(reader)
            var i = 0
            while (i < 32) {
                val entry = database.getEntry(i)
                items.add(
                    Item(
                        entry.getField(Keys.AUTHOR) ?: "UNDEFINED",
                        entry.getField(Keys.TITLE) ?: "UNDEFINED",
                        entry.getField(Keys.JOURNAL) ?: "UNDEFINED",
                        entry.getField(Keys.PUBLISHER) ?: "UNDEFINED",
                        entry.getField(Keys.YEAR) ?: "UNDEFINED"
                    )
                )
                i++
            }
        }
    }

    private fun reloadList() {
        initializeList()
        adapter.notifyDataSetChanged()
    }

    private class ItemAdapter(private val items: List<Item>) :
        RecyclerView.Adapter<RecyclerView.ViewHolder>() {
        override fun onCreateViewHolder(parent: ViewGroup, index: Int): RecyclerView.ViewHolder {
            return object : RecyclerView.ViewHolder(
                LayoutInflater.from(parent.context)
                    .inflate(R.layout.item, parent, false)
            ) {}
        }

        override fun onBindViewHolder(holder: RecyclerView.ViewHolder, index: Int) {
            holder.itemView.findViewById<TextView>(R.id.author).text =
                String.format("%s", items[index].author)
            holder.itemView.findViewById<TextView>(R.id.title).text =
                String.format("%s", items[index].title)
            holder.itemView.findViewById<TextView>(R.id.journal).text =
                String.format("%s", items[index].journal)
            holder.itemView.findViewById<TextView>(R.id.publisher).text =
                String.format("%s", items[index].publisher)
            holder.itemView.findViewById<TextView>(R.id.year).text =
                String.format("%s", items[index].year)
        }

        override fun getItemCount(): Int = items.size
    }
}
