package com.zl.aicodereview;

import java.io.IOException;
import java.util.List;

public interface ReviewProvider {
    ReviewReport review(List<SourceFile> files, String prompt) throws IOException;
}
