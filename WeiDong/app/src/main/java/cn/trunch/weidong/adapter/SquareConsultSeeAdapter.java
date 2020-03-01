package cn.trunch.weidong.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CircleCrop;
import com.lzy.ninegrid.ImageInfo;
import com.lzy.ninegrid.NineGridView;
import com.lzy.ninegrid.preview.NineGridViewClickAdapter;

import java.util.ArrayList;
import java.util.List;

import cn.trunch.weidong.R;
import cn.trunch.weidong.vo.ComUserVO;

import static com.bumptech.glide.request.RequestOptions.bitmapTransform;


public class SquareConsultSeeAdapter extends RecyclerView.Adapter<SquareConsultSeeAdapter.ViewHolder> {

    private Context context;
    private List<ComUserVO> comments = new ArrayList<>();

    static class ViewHolder extends RecyclerView.ViewHolder {

        private View consultSeeView;
        private ImageView consultSeeUHead;
        private TextView consultSeeUInfo;
        private TextView consultSeeContent;
        private NineGridView consultSeeImg;
        private TextView consultSeeInfo;

        public ViewHolder(@NonNull View view) {
            super(view);
            consultSeeView = view;
            consultSeeUHead = view.findViewById(R.id.consultSeeItemUHead);
            consultSeeUInfo = view.findViewById(R.id.consultSeeItemUInfo);
            consultSeeContent = view.findViewById(R.id.consultSeeItemContent);
            consultSeeImg = view.findViewById(R.id.consultSeeItemImg);
            consultSeeInfo = view.findViewById(R.id.consultSeeItemInfo);
        }
    }

    public SquareConsultSeeAdapter(Context context) {
        this.context = context;
    }

    public void initData(List<ComUserVO> comments) {
        if (comments != null) {
            this.comments.clear();
            this.comments.addAll(comments);
            notifyDataSetChanged();
        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(context).inflate(R.layout.view_square_consult_see_item, viewGroup, false);
        ViewHolder viewHolder = new ViewHolder(view);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        ComUserVO comment = comments.get(i);
        Glide.with(context)
                .load(comment.getUser().getuAvatar())
                .apply(bitmapTransform(new CircleCrop()))
                .into(viewHolder.consultSeeUHead);
        if (1 == comment.getComType()) {
            viewHolder.consultSeeUInfo.setText(comment.getUser().getuNickname() + " 的提问");
        } else {
            viewHolder.consultSeeUInfo.setText(comment.getUser().getuNickname() + " 的回答");
        }
        viewHolder.consultSeeContent.setText(comment.getComContent());
        List<ImageInfo> imageInfos = new ArrayList<>();
        List<String> imgs = comment.getImgs();
        if (imgs != null)
            for (String imageURL : imgs) {
                ImageInfo imageInfo = new ImageInfo();
                imageInfo.setThumbnailUrl(imageURL);
                imageInfo.setBigImageUrl(imageURL);
                imageInfos.add(imageInfo);
            }
        viewHolder.consultSeeImg.setAdapter(new NineGridViewClickAdapter(context, imageInfos));
        viewHolder.consultSeeInfo.setText(comment.getComTime());
    }

    @Override
    public int getItemCount() {
        return comments.size();
    }
}
