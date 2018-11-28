package com.appteq.ad.appteq;

import android.content.Context;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.res.ResourcesCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;

import java.util.HashMap;
import java.util.List;

public class PaymentExpandableListAdapter extends BaseExpandableListAdapter {
    private Context context;
    private List<String> header; // header titles
    // Child data in format of header title, child title
    private HashMap<String, List<String>> child;

    public PaymentExpandableListAdapter(Context context, List<String> listDataHeader,
                                        HashMap<String, List<String>> listChildData) {
        this.context = context;
        this.header = listDataHeader;
        this.child = listChildData;
    }

    @Override
    public Object getChild(int groupPosition, int childPosititon) {

        // This will return the child
        return this.child.get(this.header.get(groupPosition)).get(
                childPosititon);
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public View getChildView(int groupPosition, final int childPosition,
                             boolean isLastChild, View convertView, ViewGroup parent) {

        // Getting child text
        final String childText = (String) getChild(groupPosition, childPosition);

        // Inflating child layout and setting textview
        if (convertView == null) {
            LayoutInflater infalInflater = (LayoutInflater) this.context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = infalInflater.inflate(R.layout.paymentinfo, parent, false);
        }

        TextView child_text = (TextView) convertView.findViewById(R.id.paymentchild);

        child_text.setText(childText);
        child_text.setTypeface(ResourcesCompat.getFont(this.context,R.font.proximanova), Typeface.NORMAL);
        return convertView;
    }

    @Override
    public int getChildrenCount(int groupPosition) {

        // return children count
        return this.child.get(this.header.get(groupPosition)).size();
    }

    @Override
    public Object getGroup(int groupPosition) {

        // Get header position
        return this.header.get(groupPosition);
    }

    @Override
    public int getGroupCount() {

        // Get header size
        return this.header.size();
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded,
                             View convertView, ViewGroup parent) {

        // Getting header title
        String headerTitle = (String) getGroup(groupPosition);

        // Inflating header layout and setting text
        if (convertView == null) {
            LayoutInflater infalInflater = (LayoutInflater) this.context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = infalInflater.inflate(R.layout.paymentheader, parent, false);
        }

        TextView header_text = (TextView) convertView.findViewById(R.id.paymentheader);

        header_text.setText(headerTitle);

        // If group is expanded then change the text into bold and change the
        // icon
        if (isExpanded) {

           /* header_text.setCompoundDrawablesWithIntrinsicBounds(0, 0,
                    R.drawable.ic_up, 0);
            */
            header_text.setTypeface(ResourcesCompat.getFont(this.context,R.font.proximanova), Typeface.BOLD);
            Drawable img = ContextCompat.getDrawable(this.context, R.drawable.ic_up);
            img.setBounds(0, 0, 50, 50);
            header_text.setCompoundDrawables(null, null, img, null);
        } else {
            // If group is not expanded then change the text back into normal
            // and change the icon
            Drawable img = ContextCompat.getDrawable(this.context, R.drawable.ic_down);
            img.setBounds(0, 0, 50, 50);
            header_text.setCompoundDrawables(null, null, img, null);
            header_text.setTypeface(ResourcesCompat.getFont(this.context,R.font.proximanova), Typeface.NORMAL);
        }

        return convertView;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }
}
