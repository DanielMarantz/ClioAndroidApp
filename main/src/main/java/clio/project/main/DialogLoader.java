package clio.project.main;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

/**
 * Created by Daniel Marantz on 18/08/15.
 *
 * Loads dialogs for the activity
 */
public class DialogLoader {

    /**
     * Loads Matter details in a custom dialog.
     *
     * @param position The location of the Matter in the list.
     * @param adpt     The custom list adapter reference.
     * @param context  The context of a Activity.
     */
    public void loadDetails(int position, ListAdapter adpt, Context context) {
        final Dialog dialog = new Dialog(context);

        dialog.requestWindowFeature(Window.FEATURE_LEFT_ICON);
        dialog.setContentView(R.layout.matter_details);
        dialog.setTitle("DETAILS");
        dialog.setFeatureDrawableResource(Window.FEATURE_LEFT_ICON, R.drawable.clio_logo);
        dialog.setCancelable(false);
        // Set the custom dialog components - text, button
        TextView displayText = (TextView) dialog.findViewById(R.id.displayName);
        displayText.setText(adpt.getItem(position).getDisplayName());

        TextView clientText = (TextView) dialog.findViewById(R.id.clientName);
        clientText.setText(adpt.getItem(position).getClientName());

        TextView descText = (TextView) dialog.findViewById(R.id.description);
        descText.setText(adpt.getItem(position).getDescription());

        TextView openDateText = (TextView) dialog.findViewById(R.id.openDate);
        openDateText.setText(adpt.getItem(position).getOpenDate());

        TextView statusText = (TextView) dialog.findViewById(R.id.status);
        statusText.setText(adpt.getItem(position).getStatus());
        // Close dialog button
        Button closeButton = (Button) dialog.findViewById(R.id.closeDialog);
        closeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        dialog.show();
    }

    /**
     * Loads an alert dialog regarding Network && no persisted data.
     * Click event exits application.
     *
     * @param context The context of a Activity.
     */
    public void loadAlert(final Context context) {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                context);

        alertDialogBuilder.setTitle("No Saved Data Or Network Connection");
        // Set dialog message
        alertDialogBuilder
                .setMessage("Need network connection to retrieve initial data!")
                .setCancelable(false)
                .setPositiveButton("Exit", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // Close current activity
                        ((Activity)(context)).finish();
                    }
                });
        // Create alert dialog
        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }
}
