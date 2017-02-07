package net.leejjon.bluffpoker.stages;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import net.leejjon.bluffpoker.BluffPokerGame;
import net.leejjon.bluffpoker.dialogs.AddNewPlayerDialog;
import net.leejjon.bluffpoker.dialogs.WarningDialog;
import net.leejjon.bluffpoker.interfaces.ContactsRequesterInterface;
import net.leejjon.bluffpoker.listener.ChangeStageListener;
import net.leejjon.bluffpoker.listener.ModifyPlayerListener;

import java.util.ArrayList;
import java.util.Collections;

public class SelectPlayersStage extends AbstractStage implements ModifyPlayerListener {
    private java.util.List<String> players;
    private List<String> playerList;

    private WarningDialog playerAlreadyExistsWarning;
    private WarningDialog playerNameInvalid;
    private WarningDialog minimalTwoPlayersRequired;

    public SelectPlayersStage(Skin uiSkin, final ChangeStageListener changeScreen, ContactsRequesterInterface contactsRequester) {
        super(false);

        playerAlreadyExistsWarning = new WarningDialog("Player already exists.", uiSkin);
        playerNameInvalid = new WarningDialog("Player name invalid.", uiSkin);
        minimalTwoPlayersRequired = new WarningDialog("Select at least two players!", uiSkin);
        final AddNewPlayerDialog addNewPlayerDialog = new AddNewPlayerDialog(this);

        players = new ArrayList<>();

        if (contactsRequester.hasContactPermissions()) {
            players.add(contactsRequester.getDeviceOwnerName());
        }

        playerList = new List<>(uiSkin);
        playerList.setItems(players.toArray(new String[players.size()]));

        Label choosePlayersLabel = new Label("Choose players", uiSkin, "console32", Color.WHITE);

        ScrollPane playersScrollPane = new ScrollPane(playerList, uiSkin);
        playersScrollPane.setScrollingDisabled(true, false);

        float padding = 10f;

        table.center();
        table.add(choosePlayersLabel).colspan(2).padBottom(padding);
        table.row();

        int width = Gdx.graphics.getWidth() / BluffPokerGame.getDivideScreenByThis();
        int height = Gdx.graphics.getHeight() / BluffPokerGame.getDivideScreenByThis();

        // Take 50% of the screen.
        table.add(playersScrollPane).colspan(2).width((width * 100) / 170)
                .height((height * 100) / 200)
                .padBottom(padding);
        table.row();

        TextButton up = new TextButton("Move up", uiSkin);
        up.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                swapPlayerUp();
            }
        });
        TextButton down = new TextButton("Move down", uiSkin);
        down.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                swapPlayerDown();
            }
        });
        TextButton delete = new TextButton("Remove", uiSkin);
        delete.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                removeSelectedPlayer();
            }
        });
        TextButton enterNew = new TextButton("Enter new", uiSkin);
        enterNew.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                Gdx.input.getTextInput(addNewPlayerDialog, "Insert new player name", "", "Enter name here.");
            }
        });
        TextButton phonebook = new TextButton("Phonebook", uiSkin);
        phonebook.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                if (contactsRequester.hasContactPermissions()) {
                    // TODO: Open contacts dialog.
                } else {
                    contactsRequester.requestContactPermission();
                }
            }
        });
        TextButton startGame = new TextButton("Start game", uiSkin);
        startGame.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                startGame(changeScreen);
            }
        });

        table.add(up).left().width(down.getWidth()).padBottom(padding/2);
        table.add(enterNew).right().width(phonebook.getWidth()).padBottom(padding/2);
        table.row();
        table.add(down).left().padBottom(padding/2);
        table.add(phonebook).right().padBottom(padding/2);
        table.row();
        table.add(delete).left().width(down.getWidth());
        table.add(startGame).right().width(phonebook.getWidth());

        addActor(table);
    }

    protected void startGame(ChangeStageListener changeScreen) {
        if (players.size() < 2) {
            minimalTwoPlayersRequired.show(this);
        } else {
            changeScreen.startGame(players);
        }
    }

    @Override
    public void addNewPlayer(String playerName) {
        final int maxNameLength = 16;

        if (playerName.length() > 0 && playerName.length() <= maxNameLength) {
            if (!players.contains(playerName)) {
                players.add(playerName);
                playerList.setItems(players.toArray(new String[players.size()]));
            } else {
                playerAlreadyExistsWarning.show(this);
            }
        } else {
            playerNameInvalid.show(this);
        }
    }

    private void swapPlayerUp() {
        int selectedIndex = playerList.getSelectedIndex();
        if (selectedIndex > 0) {
            Collections.swap(players, selectedIndex, selectedIndex - 1);
            playerList.setItems(players.toArray(new String[players.size()]));
        }
    }

    private void swapPlayerDown() {
        int selectedIndex = playerList.getSelectedIndex();
        if (selectedIndex > -1 && selectedIndex < players.size() - 1 && players.size() > 1) {
            Collections.swap(players, selectedIndex, selectedIndex + 1);
            playerList.setItems(players.toArray(new String[players.size()]));
        }
    }

    private void removeSelectedPlayer() {
        String selectedPlayer = playerList.getSelected();
        if (selectedPlayer != null) {
            players.remove(selectedPlayer);
            playerList.setItems(players.toArray(new String[players.size()]));
        }
    }
}
