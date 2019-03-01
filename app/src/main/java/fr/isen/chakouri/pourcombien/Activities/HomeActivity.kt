package fr.isen.chakouri.pourcombien.Activities

import android.app.ActionBar
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.widget.LinearLayout
import android.view.ViewGroup
import android.widget.EditText
import kotlinx.android.synthetic.main.activity_home.*

class HomeActivity : AppCompatActivity() {

    var numberOfLines = 4

    public override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(fr.isen.chakouri.pourcombien.R.layout.activity_home)

        addFieldButton.setOnClickListener{
            addLine()
        }
    }

    private fun createEditText(): EditText {
        val lparams = ActionBar.LayoutParams(698, 170) // Width , height
        val edittext = EditText(this)
        edittext.layoutParams = lparams
        edittext.setHint(" Joueur  " + numberOfLines)
        return edittext
    }

    fun addLine() {
        val p = LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
        createEditText().layoutParams = p
        createEditText().id = numberOfLines + 1
        joueursLayout.addView(createEditText())
        numberOfLines++
    }
    /*
    fun onAddField(v: View) {
        val inflater = getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val editText =
        // Add the new row before the add field button.
        parentLinearLayout!!.addView(rowView, parentLinearLayout!!.childCount - 1)
    }

    fun onDelete(v: View) {
        parentLinearLayout!!.removeView(v.getParent() as View)
    }
    */
}
