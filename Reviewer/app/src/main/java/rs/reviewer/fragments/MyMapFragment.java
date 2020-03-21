package rs.reviewer.fragments;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
import androidx.fragment.app.Fragment;
import rs.reviewer.R;

public class MyMapFragment extends Fragment {
	public static MyMapFragment newInstance() {
		return new MyMapFragment();
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup vg, Bundle data) {
		return inflater.inflate(R.layout.map_layout, vg, false);
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		Toast.makeText(getActivity(), "onActivityCreated()", Toast.LENGTH_SHORT).show();
	}

	@Override
	public void onAttach(Context context) {
		super.onAttach(context);
		Toast.makeText(getActivity(), "onAttach()", Toast.LENGTH_SHORT).show();
	}

	@Override
	public void onDestroyView() {
		super.onDestroyView();
		Toast.makeText(getActivity(), "onDestroyView()", Toast.LENGTH_SHORT).show();
	}

	@Override
	public void onDetach() {
		super.onDetach();
		Toast.makeText(getActivity(), "onDeatach()", Toast.LENGTH_SHORT).show();
	}
}