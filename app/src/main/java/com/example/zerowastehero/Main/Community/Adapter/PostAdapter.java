//package com.example.zerowastehero.Main.Community.Adapter;
//
//import android.view.LayoutInflater;
//import android.view.ViewGroup;
//
//import androidx.annotation.NonNull;
//import androidx.recyclerview.widget.RecyclerView;
//
//import com.example.zerowastehero.DataBinding.Model.PostModel;
//import com.example.zerowastehero.databinding.ItemPostBinding;
//
//import java.util.List;
//
//public class PostAdapter extends  RecyclerView.Adapter<PostAdapter.PostViewHolder> {
//    private List<PostModel> posts;
//
//    public PostAdapter(List<PostModel> posts) {
//        this.posts = posts;
//    }
//
//    @NonNull
//    @Override
//    public PostViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
//        // Inflate the item layout (item_post.xml)
//        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
//        ItemPostBinding binding = ItemPostBinding.inflate(inflater, parent, false);
//        return new PostViewHolder(binding);
//    }
//
//    @Override
//    public void onBindViewHolder(@NonNull PostViewHolder holder, int position) {
//        // Bind the post data to the view
//        PostModel post = posts.get(position);
//        holder.bind(post);
//    }
//
//    @Override
//    public int getItemCount() {
//        return posts.size();
//    }
//
//    static class PostViewHolder extends RecyclerView.ViewHolder {
//        private final ItemPostBinding binding;
//
//        public PostViewHolder(@NonNull ItemPostBinding binding) {
//            super(binding.getRoot());
//            this.binding = binding;
//        }
//
//        public void bind(PostModel post) {
//            binding.setPost(post);
//            binding.executePendingBindings();
//        }
//    }
//}
