package com.amuyana.app.data.tod;

import com.amuyana.app.data.Dynamism;
import com.amuyana.app.data.Register;
import com.amuyana.app.data.Syllogism;
import com.amuyana.app.node.NodeHandler;
import com.amuyana.app.node.tod.expression.ConjunctionExp;
import com.amuyana.app.node.tod.expression.Expression;
import com.amuyana.app.node.tod.expression.ImplicationExp;
import com.amuyana.app.node.tod.expression.SyllogismExp;
import javafx.geometry.Pos;

import java.sql.Timestamp;
import java.time.Duration;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.chrono.ChronoLocalDateTime;
import java.time.temporal.ChronoUnit;
import java.time.temporal.Temporal;
import java.time.temporal.TemporalAmount;
import java.util.List;

public class RegisterTableEntry {
    private String id;
    private String date;
    private ImplicationExp dynamismName;
    private ConjunctionExp conjunctionExp;
    private String start;
    private String end;
    private String interval;

    public RegisterTableEntry(Register register) {
        LocalDateTime startLocalDateTime = register.getStart().toLocalDateTime();
        LocalDateTime endLocalDateTime = register.getEnd().toLocalDateTime();

        id=String.valueOf(register.getIdRegister());
        date=register.getDate().toString();
        dynamismName = new ImplicationExp(register.getDynamism(), Expression.ExpressionType.PROPOSITION, Pos.CENTER);
        conjunctionExp = new ConjunctionExp(register.getDynamism());
        start = register.getStart().toString();
        end = register.getEnd().toString();

        long seconds = ChronoUnit.SECONDS.between(startLocalDateTime,endLocalDateTime);
        long minutes = ChronoUnit.MINUTES.between(startLocalDateTime,endLocalDateTime);
        long hours = ChronoUnit.HOURS.between(startLocalDateTime,endLocalDateTime);
        long days = ChronoUnit.DAYS.between(startLocalDateTime,endLocalDateTime);
        long weeks = ChronoUnit.WEEKS.between(startLocalDateTime,endLocalDateTime);
        long months = ChronoUnit.MONTHS.between(startLocalDateTime,endLocalDateTime);
        long years = ChronoUnit.YEARS.between(startLocalDateTime,endLocalDateTime);

        if (seconds < 360) {
            interval = seconds + " seconds";
        }else if (seconds >= 360) {
            if (minutes < 360) {
                interval = minutes + " minutes";
            } else if (minutes >= 360) {
                if (hours < 48) {
                    interval = hours + " hours";
                } else if (hours >= 48) {
                    if (days < 28) {
                        interval = days + " days";
                    } else if (days >= 28) {
                        if (weeks < 52) {
                            interval = weeks + " weeks";
                        } else if (weeks >= 52) {
                            if (months < 24) {
                                interval = months + " months";
                            } else if (months >= 24) {
                                interval = years + " years";
                            }
                        }
                    }
                }
            }
        }


    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getStart() {
        return start;
    }

    public void setStart(String start) {
        this.start = start;
    }

    public String getEnd() {
        return end;
    }

    public void setEnd(String end) {
        this.end = end;
    }

    public String getInterval() {
        return interval;
    }

    public void setInterval(String interval) {
        this.interval = interval;
    }

    public ConjunctionExp getConjunctionExp() {
        return conjunctionExp;
    }

    public void setConjunctionExp(ConjunctionExp conjunctionExp) {
        this.conjunctionExp = conjunctionExp;
    }

    public ImplicationExp getDynamismName() {
        return dynamismName;
    }

    public void setDynamismName(ImplicationExp dynamismName) {
        this.dynamismName = dynamismName;
    }
}
