package edu.cnm.deepdive.eb.flashme.adapters;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import edu.cnm.deepdive.eb.flashme.R;
import edu.cnm.deepdive.eb.flashme.activities.DeckMemberActivity;
import edu.cnm.deepdive.eb.flashme.entities.Deck;

import static edu.cnm.deepdive.eb.flashme.fragments.DeckMemberFragment.DECK;
import static edu.cnm.deepdive.eb.flashme.fragments.DeckMemberFragment.DECK_ID;

/**
 * Manages my the Recycler view for the deck name list.
 */
public class DeckListRecyclerViewAdapter
    extends RecyclerView.Adapter<DeckListRecyclerViewAdapter.ViewHolder> {

  /**
   * Contains a list of individual deck.
   */
  private final List<Deck> Decks;

  private List<Long> deckDeletePool = new ArrayList<>();

  /**
   * Constructor for the List<deck> Decks field.
   * @param decks Passes user created decks
   */
  public DeckListRecyclerViewAdapter(List<Deck> decks) {
    Decks = decks;
  }

  @Override
  public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
    View view = LayoutInflater.from(parent.getContext())
        .inflate(R.layout.deck_list_content, parent, false);
    return new ViewHolder(view);
  }

  @Override
  public void onBindViewHolder(final ViewHolder holder, final int position) {
    holder.deck = Decks.get(position);
    holder.deckView.setText(holder.deck.getName());
    holder.deckPool.setText(String.valueOf(holder.deck.getPool()));

    holder.deckDeleteView.setOnClickListener(new OnClickListener() {
      @Override
      public void onClick(View v) {
//        deckDeletePool.add(Decks.get(position).getId());
      }
    });

    /** Passes the value of the deck id onto DeckMemberActivity */
    holder.deckView.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        Context context = v.getContext();
        Intent intent = new Intent(context, DeckMemberActivity.class);
        intent.putExtra(DECK_ID, holder.deck.getId());
        intent.putExtra(DECK, holder.deck.getName());
        context.startActivity(intent);
      }
    });
  }

  // Adapter connects me to data, its job is to construct
  // a collection of views object to be used by a list/recycler view
  @Override
  public int getItemCount() {
    return Decks.size();
  }


  // construct a representation for a deck in the list
  public class ViewHolder extends RecyclerView.ViewHolder {

    public final View view;
    public final TextView deckView;
    public final CheckBox deckDeleteView;
    public final TextView deckPool;
    public Deck deck;

    public ViewHolder(View view) {
      super(view);
      this.view = view;
      deckView = view.findViewById(R.id.deck_name);
      deckPool = view.findViewById(R.id.deck_pool);
      deckDeleteView = view.findViewById(R.id.delete_deck);

    }

    @Override
    public String toString() {
      return super.toString() + " '" + deckView.getText() + "'";
    }
  }


  public List<Long> getDeckDeletePool() {
    return deckDeletePool;
  }

  }
