package se.warting.masterdetailbinding;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import se.warting.masterdetailbinding.databinding.ActivityItemListBinding;
import se.warting.masterdetailbinding.databinding.ItemListContentBinding;
import se.warting.masterdetailbinding.dummy.DummyContent;

/**
 * An activity representing a list of Items. This activity
 * has different presentations for handset and tablet-size devices. On
 * handsets, the activity presents a list of items, which when touched,
 * lead to a {@link ItemDetailActivity} representing
 * item details. On tablets, the activity presents the list of items and
 * item details side-by-side using two vertical panes.
 */
public class ItemListActivity extends AppCompatActivity {

    /**
     * Whether or not the activity is in two-pane mode, i.e. running on a tablet
     * device.
     */
    private boolean mTwoPane;
    private ActivityItemListBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_list);

        binding = DataBindingUtil.setContentView(this, R.layout.activity_item_list);

        setSupportActionBar(binding.toolbar);
        binding.toolbar.setTitle(getTitle());


        binding.fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });


        setupRecyclerView(binding.includedList.itemList);

        if (binding.includedList.itemDetailContainer != null) {
            // The detail container view will be present only in the
            // large-screen layouts (res/values-w900dp).
            // If this view is present, then the
            // activity should be in two-pane mode.
            mTwoPane = true;
        }
    }

    private void setupRecyclerView(@NonNull RecyclerView recyclerView) {
        recyclerView.setAdapter(new SimpleItemRecyclerViewAdapter(DummyContent.ITEMS));
    }

    public class SimpleItemRecyclerViewAdapter
            extends RecyclerView.Adapter<SimpleItemRecyclerViewAdapter.ViewHolder> {

        private final List<DummyContent.DummyItem> mValues;

        public SimpleItemRecyclerViewAdapter(List<DummyContent.DummyItem> items) {
            mValues = items;
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

            ItemListContentBinding view = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.item_list_content, parent, false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(final ViewHolder holder, int position) {
            holder.mItem = mValues.get(position);
            holder.mView.setItem(mValues.get(position));
            holder.mView.setHandlers(new MyHandlers(holder.mItem.id));
            holder.mView.getRoot().setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                }
            });
        }

        @Override
        public int getItemCount() {
            return mValues.size();
        }

        public class ViewHolder extends RecyclerView.ViewHolder {
            public final ItemListContentBinding mView;
            public DummyContent.DummyItem mItem;

            public ViewHolder(ItemListContentBinding view) {
                super(view.getRoot());
                mView = view;
            }

            @Override
            public String toString() {
                return super.toString() + " '" + mView.content.getText() + "'";
            }
        }

    }

    public class MyHandlers {
        private final String id;

        public MyHandlers(String id) {
            this.id = id;
        }

        public void onListItemClicked(View view) {
            if (mTwoPane) {
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.item_detail_container, ItemDetailFragment.createFragment(id))
                        .commit();
            } else {
                Intent intent = new Intent(ItemListActivity.this, ItemDetailActivity.class);
                intent.putExtra(ItemDetailFragment.ARG_ITEM_ID, id);

                startActivity(intent);
            }
        }
    }
}
