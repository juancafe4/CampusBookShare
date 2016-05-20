package com.polybookshare.master.campusbookshare;

import android.content.Context;
import android.view.LayoutInflater;
import android.widget.RelativeLayout;
import android.widget.TextView;

import org.w3c.dom.Text;

/**
 * Created by MASTER on 5/3/16.
 */
public class KlassView extends RelativeLayout {
    private Klass m_klass;
    private TextView teacher;
    private TextView comment;
    private TextView details;
    private TextView klass_name;



    public KlassView(Context context, Klass klass) {
        super(context);
        LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.klass_view, this, true);

        klass_name = (TextView) findViewById(R.id.class_name);
        teacher = (TextView) findViewById(R.id.teacher);
        comment = (TextView) findViewById(R.id.details);
        details = (TextView) findViewById(R.id.comments);

        setKlass(klass);
    }

    public void setKlass(Klass klass) {
        String details = "";
        this.m_klass = klass;

        details = this.m_klass.getFiles() + " files(s)\n"
                + this.m_klass.getComments() + " comments(s)\n"
                + this.m_klass.getReviews() + " reviews(s)\n";

        comment.setText(details);
        this.details.setText(this.m_klass.getLastReview());
        teacher.setText(this.m_klass.getProfessor());
        klass_name.setText(this.m_klass.getDep() + this.m_klass.getNumber());
    }

    public Klass getKlass() {
        return this.m_klass;
    }
}
