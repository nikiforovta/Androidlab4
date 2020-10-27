package com.example.lab4task2

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
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        initializeList()
        val recycler = findViewById<RecyclerView>(R.id.recycler)
        recycler.setHasFixedSize(true)
        recycler.layoutManager = LinearLayoutManager(this)
        recycler.adapter = adapter
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
                        entry.getField(Keys.AUTHOR),
                        entry.getField(Keys.TITLE),
                        entry.getField(Keys.JOURNAL),
                        entry.getField(Keys.PUBLISHER) ?: "UNDEFINED",
                        entry.getField(Keys.YEAR)
                    )
                )
                i++
            }
        }
    }

    class ItemAdapter(private val items: List<Item>) :
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