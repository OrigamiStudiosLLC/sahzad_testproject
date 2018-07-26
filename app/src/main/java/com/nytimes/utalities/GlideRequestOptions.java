package com.nytimes.utalities;

import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.nytimes.R;


public class GlideRequestOptions {

    public static final RequestOptions profileImageOptions = new RequestOptions()
            .dontAnimate()
            .placeholder(R.drawable.ic_image_place_holder)
            .diskCacheStrategy(DiskCacheStrategy.ALL);

    public static final RequestOptions mapsMarkerOptions = new RequestOptions()
            .diskCacheStrategy(DiskCacheStrategy.RESOURCE)
            .placeholder(R.drawable.ic_image_place_holder);

    public static final RequestOptions imageOptions = new RequestOptions()
            .dontAnimate()
            .placeholder(R.drawable.ic_image_place_holder)
            .diskCacheStrategy(DiskCacheStrategy.ALL);

    public static final RequestOptions defaultPlaceHolderOptions = new RequestOptions()
            .dontAnimate()
            .placeholder(R.drawable.ic_image_place_holder)
            .diskCacheStrategy(DiskCacheStrategy.ALL);

}
