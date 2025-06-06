package com.amit_g.helper;

import android.annotation.SuppressLint;
import android.os.Build;

import com.google.android.material.datepicker.CalendarConstraints;
import com.google.android.material.datepicker.CompositeDateValidator;
import com.google.android.material.datepicker.DateValidatorPointBackward;
import com.google.android.material.datepicker.DateValidatorPointForward;

import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.Period;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.chrono.IsoChronology;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.time.format.ResolverStyle;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

// Utility class for date and time conversion, formatting, and constraints
public class DateUtil {

    // Converts date string to LocalDate with default pattern
    public static LocalDate stringToLocalDate(String date){
        return DateUtil.stringToLocalDate(date, "d/M/uuuu" );
    }

    // Converts string to LocalDate using a custom pattern
    public static LocalDate stringToLocalDate(String date, String datePattern){
        LocalDate localDate = null;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern(datePattern)
                    .withChronology(IsoChronology.INSTANCE)
                    .withResolverStyle(ResolverStyle.STRICT);
            try {
                localDate = LocalDate.parse(date, formatter);
            } catch (Exception e) {}
        }
        return localDate;
    }

    // Converts LocalDate to formatted string with default pattern
    public static String locaDateToString(LocalDate date){
        return DateUtil.locaDateToString(date, "dd/MM/uuuu");
    }

    // Converts LocalDate to string using a custom pattern
    public static String locaDateToString(LocalDate date, String datePattern){
        String dateStr = "";
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern(datePattern)
                    .withChronology(IsoChronology.INSTANCE)
                    .withResolverStyle(ResolverStyle.STRICT);
            try {
                dateStr = date.format(formatter);
            } catch (Exception e) {}
        }
        return dateStr;
    }

    // Converts a date string to epoch milliseconds
    public static long stringDateToLong(String date){
        return DateUtil.localDateToLong(DateUtil.stringToLocalDate(date));
    }

    // Converts epoch milliseconds to formatted date string
    public static String longDateToString(long date){
        return DateUtil.locaDateToString(DateUtil.longToLocalDate(date));
    }

    // Converts LocalDate to milliseconds since epoch
    @SuppressLint("NewApi")
    public static long localDateToLong(LocalDate date){
        ZoneId zoneId = ZoneId.systemDefault();
        LocalDateTime localDateTime = date.atStartOfDay();
        Instant instant = localDateTime.atZone(zoneId).toInstant();
        return instant.toEpochMilli();
    }

    // Converts milliseconds to LocalDate
    @SuppressLint("NewApi")
    public static LocalDate longToLocalDate(long millis) {
        Instant instant = Instant.ofEpochMilli(millis);
        return instant.atZone(ZoneId.systemDefault()).toLocalDate();
    }

    // Converts LocalDate to Date
    public static Date localDateToDate(LocalDate localDate) {
        if (localDate == null) return null;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            return Date.from(localDate.atStartOfDay(ZoneId.systemDefault()).toInstant());
        } else return null;
    }

    // Converts Date to LocalDate
    public static LocalDate dateToLocalDate(Date date) {
        if (date == null) return null;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            return date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        } else return null;
    }

    // Converts timestamp to readable date string
    public static String formatDate(long timestamp) {
        SimpleDateFormat sdf = new SimpleDateFormat("MMMM dd, yyyy", Locale.getDefault());
        return sdf.format(new Date(timestamp));
    }

    // Checks if a date is within a range
    public static boolean inRange(LocalDate dateToCheck, LocalDate startRange, LocalDate endRange){
        return DateUtil.inRange(dateToCheck, startRange, endRange, true);
    }

    public static boolean inRange(LocalDate dateToCheck, LocalDate startRange, LocalDate endRange, boolean includeEdges) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            if (includeEdges) {
                return (dateToCheck.isEqual(startRange) || dateToCheck.isAfter(startRange)) &&
                        (dateToCheck.isEqual(endRange) || dateToCheck.isBefore(endRange));
            } else {
                return dateToCheck.isAfter(startRange) && dateToCheck.isBefore(endRange);
            }
        }
        return true;
    }

    // Enum for age formatting options
    public enum Periods {YEARS, MONTHS, DAYS, YEARS_MONTHS, YEAR_MONTHS_DAYS};

    // Returns age string from date of birth to now
    public static String age(LocalDate dateOfBirth, Periods periods){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            return DateUtil.age(dateOfBirth, LocalDate.now(), periods);
        } else return "-1";
    }

    // Returns age string between two dates based on given format
    public static String age(LocalDate fromDate, LocalDate toDate, Periods periods){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            Period period = Period.between(fromDate, toDate);
            switch (periods){
                case YEARS: return String.valueOf(period.getYears());
                case MONTHS: return String.valueOf(period.getMonths());
                case DAYS: return String.valueOf(period.getDays());
                case YEAR_MONTHS_DAYS: return period.getYears() + "|" + period.getMonths() + "|" + period.getDays();
                case YEARS_MONTHS: return period.getYears() + "|" + period.getMonths();
                default: return null;
            }
        }
        return "";
    }

    // Builds date constraints for material date picker
    public static CalendarConstraints buidCalendarConstrains(LocalDate startDate, LocalDate endDate){
        return buidCalendarConstrains(startDate, endDate, null);
    }

    // Builds date constraints with optional open date
    public static CalendarConstraints buidCalendarConstrains(LocalDate startDate, LocalDate endDate, LocalDate openAtDate){
        long dateStart = 0;
        long dateEnd = 0;
        long openAt = 0;

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            dateStart = ZonedDateTime.ofLocal(startDate.atStartOfDay(), ZoneId.systemDefault(), ZoneOffset.ofHours(0)).toInstant().toEpochMilli();
            dateEnd = ZonedDateTime.ofLocal(endDate.atStartOfDay(), ZoneId.systemDefault(), ZoneOffset.ofHours(0)).toInstant().toEpochMilli();
            if (openAtDate != null)
                openAt = ZonedDateTime.ofLocal(openAtDate.atStartOfDay(), ZoneId.systemDefault(), ZoneOffset.ofHours(0)).toInstant().toEpochMilli();
        }

        CalendarConstraints.DateValidator min = DateValidatorPointForward.from(dateStart);
        CalendarConstraints.DateValidator max = DateValidatorPointBackward.before(dateEnd);

        ArrayList<CalendarConstraints.DateValidator> validatorsList = new ArrayList<>();
        validatorsList.add(min);
        validatorsList.add(max);

        CalendarConstraints.DateValidator combinedValidator = CompositeDateValidator.allOf(validatorsList);

        CalendarConstraints.Builder builder = new CalendarConstraints.Builder();
        builder.setValidator(combinedValidator);
        builder.setStart(dateStart);
        builder.setEnd(dateEnd);
        builder.setOpenAt(openAtDate != null ? openAt : dateStart);

        return builder.build();
    }

    // Converts LocalTime to milliseconds
    @SuppressLint("NewApi")
    public static long localTimeToLong(LocalTime time) {
        return time.toSecondOfDay() * 1000L;
    }

    // Converts milliseconds to LocalTime
    @SuppressLint("NewApi")
    public static LocalTime longToLocalTime(long millis) {
        return LocalTime.ofSecondOfDay((int)(millis / 1000));
    }

    // Formats LocalTime to HH:mm string
    @SuppressLint("NewApi")
    public static String localTimeToString(LocalTime time) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm", Locale.getDefault());
        return time.format(formatter);
    }

    // Calculates baby's age in months given birth date and reference date
    public static int getAgeInMonths(long birthDateMillis, long referenceDateMillis) {
        Calendar birthDate = Calendar.getInstance();
        birthDate.setTimeInMillis(birthDateMillis);

        Calendar referenceDate = Calendar.getInstance();
        referenceDate.setTimeInMillis(referenceDateMillis);

        int yearsDiff = referenceDate.get(Calendar.YEAR) - birthDate.get(Calendar.YEAR);
        int monthsDiff = referenceDate.get(Calendar.MONTH) - birthDate.get(Calendar.MONTH);

        int totalMonths = yearsDiff * 12 + monthsDiff;

        if (referenceDate.get(Calendar.DAY_OF_MONTH) < birthDate.get(Calendar.DAY_OF_MONTH)) {
            totalMonths--;
        }

        return Math.max(totalMonths, 0);
    }
    @SuppressLint("NewApi")
    public static DateTimeFormatter getFormatter() {
        return DateTimeFormatter.ofPattern("dd/MM/yyyy");
    }



}
