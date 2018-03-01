# support

```
presenter = ListPresenter.<String>create()
                .addViewBinder(String.class.hashCode(), new SimpleViewBound(BR.data, R.layout.item_main))
                .diffCallback(new ListPresenter.DiffCallback<String>() {

                    @Override
                    public boolean areItemsTheSame(String s, String t1) {
                        return s.equals(t1);
                    }

                    @Override
                    public boolean areContentsTheSame(String s, String t1) {
                        return s.equals(t1);
                    }
                })
                .attachWithBound(recyclerView);

        presenter.display(list);

```