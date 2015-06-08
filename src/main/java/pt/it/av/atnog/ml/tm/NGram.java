package pt.it.av.atnog.ml.tm;

/**
 * Created by mantunes on 6/1/15.
 */
public class NGram {
    protected final String array[];

    public NGram(String array[]) {
        this.array =  array;
    }

    public NGram(int size) {
        this.array = new String[size];
    }

    public int size() {
        return array.length;
    }

    public int levenshtein(NGram ngram) {
        int rv = 0;

        return rv;
    }

    @Override
    public boolean equals(Object o) {
        boolean rv = false;
        if (o != null) {
            if (o == this)
                rv = true;
            else if (o instanceof NGram) {
                NGram ngram = (NGram) o;
                if (ngram.array.length == this.array.length) {
                    rv = true;
                    for(int i = 0;  i < this.array.length && rv; i++)
                        rv = this.array[i].equals(ngram.array[i]);
                } else
                    rv = false;
            }
        }
        return rv;
    }
}
