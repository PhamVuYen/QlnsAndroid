package com.example.qlnv.adapter;

import java.util.ArrayList;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.qlnv.R;
import com.example.qlnv.model.Employee;
import com.example.qlnv.model.Room;

public class RoomAdapter extends ArrayAdapter<Room> {
    Activity context;
    int layoutId;
    public ArrayList<Room> arrRoom;

    public RoomAdapter(Activity context, int textViewResourceId,
                       ArrayList<Room> objects) {
        super(context, textViewResourceId, objects);
        this.context = context;
        this.layoutId = textViewResourceId;
        this.arrRoom = objects;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        convertView = context.getLayoutInflater().inflate(layoutId, null);
        TextView txtpb = (TextView) convertView.findViewById(R.id.txtShortInfor);
        TextView txtmotapb = (TextView) convertView.findViewById(R.id.txtDetailInfor);
        Room pb = arrRoom.get(position);
        txtpb.setText(pb.toString());
        String strMota = "";
        String tp = "Trưởng Phòng: [Chưa có]";
        Employee nv = pb.getTruongPhong();
        if (nv != null) {
            tp = "Trưởng Phòng:" + nv.getName();
        }

//		ArrayList<Employee> dsPp=pb.getPhoPhong();
//		String pp="Phó phòng: [Chưa có]";
//		if(dsPp.size()>0)
//		{
//			pp="Phó phòng:\n";
//			for(int i=0;i<dsPp.size();i++)
//			{
//				pp+=(i+1)+". "+dsPp.get(i).getName()+"\n";
//			}
//		}
        strMota = tp;
        txtmotapb.setText(strMota);
        return convertView;
    }

    public ArrayList<Room> getArrPhongBan() {
        return arrRoom;
    }


}
