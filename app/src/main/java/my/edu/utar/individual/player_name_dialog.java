package my.edu.utar.individual;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatDialogFragment;

public class player_name_dialog extends AppCompatDialogFragment {
    private EditText EditPlayerName;
    private Button EnterPlayerName;
    private ExampleDialogListener listener;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.insert_player_name, null);


        builder.setView(view);

        EditPlayerName = view.findViewById(R.id.playerName);
        EnterPlayerName = view.findViewById(R.id.playerSubmit);

        EnterPlayerName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String username = EditPlayerName.getText().toString();


                // get the name and scores
                GameResult.ScoreList_finalize.add(GameResult.scores);
                GameResult.NameList_finalize.add(username);

                // Update the list for name and scores
                GameResult.sorting();
                GameResult.updateRanking(GameResult.context.getApplicationContext());

                listener.applyTexts(username);
                getDialog().dismiss();
            }
        });

        return builder.create();
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);

        try {
            listener = (ExampleDialogListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString() +
                    "must implement Example Dialog Listener");
        }
    }

    public interface ExampleDialogListener {
        String applyTexts (String player) ;
    }
}
