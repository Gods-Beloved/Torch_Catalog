package com.gmail.jamesgodwillarkoh.torchcatalog

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.facebook.shimmer.ShimmerFrameLayout
import com.parse.FindCallback
import com.parse.ParseObject
import com.parse.ParseQuery
import org.json.JSONArray

class CourseAdapter(val context: Context?): RecyclerView.Adapter<CourseAdapter.CourseViewHolder>() {

   private lateinit var mListener: OnItemClickListener


    var networkerror:Boolean=false



    interface  OnItemClickListener{
        fun onItemClick( position: Int,intent: Intent,intentLecture:Intent)
    }


    fun setOnItemClickListener(listener: OnItemClickListener){
mListener=listener
    }


    class CourseViewHolder( itemView: View) : RecyclerView.ViewHolder(itemView) {





        val query: ParseQuery<ParseObject> =ParseQuery.getQuery<ParseObject>("Course")






       val courseLecturer:TextView=itemView.findViewById(R.id.v_couse_lecturer)

        val courseCode:TextView=itemView.findViewById(R.id.v_coursecode)

        val shim:ShimmerFrameLayout=itemView.findViewById(R.id.v_shimmer_load2)


    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CourseViewHolder {


        val v=LayoutInflater.from(context).inflate(R.layout.course_item,parent,false)





        return CourseViewHolder(v)
    }





    override fun getItemCount(): Int {
        val size=ParseQuery.getQuery<ParseObject>("Course")


        return size.count()
    }

    override fun onBindViewHolder(holder: CourseViewHolder, position: Int) {
//       holder.courseCode.text= arrayCourse.getJSONArray(position).toString()
//        holder.courseLecturer.text= arrayLecturers.getJSONArray(position).toString()

      //  val courseCode=holder.courseCode.text.trim().toString()

    holder.query.findInBackground { objects, e ->
        if (e==null)
        {
            holder.shim.hideShimmer()
            val name=objects[position].getString("lecturer")
            val code=objects[position].getString("code")
            holder.courseCode.text= code
            holder.courseLecturer.text=name
        }

        else{
            holder.shim.hideShimmer()
            networkerror=true


        }

    }



        holder.itemView.setOnClickListener {
            val position2=holder.adapterPosition

           val value=holder.courseCode.text.trim().toString()

            val intent=Intent(context,Scanner::class.java)
            val intentLecture=Intent(context,StudentList::class.java)
            intent.putExtra("courseCode",value)
            intentLecture.putExtra("courseCode",value)



            mListener.onItemClick(position2,intent,intentLecture)
        }
//



    }
}