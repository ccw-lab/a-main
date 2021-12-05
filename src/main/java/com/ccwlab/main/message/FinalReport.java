package com.ccwlab.main.message;

import java.time.Instant;

public class FinalReport {
    FinalReportStatus status;
    String log;
    Instant createdTime;
}
enum FinalReportStatus{
    Completed,
    Failed
}