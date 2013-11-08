package com.ams.reminisce;

import java.util.Calendar;

import android.app.Activity;

public class CalendarActivity extends Activity{
  public static void main(String args[]){
    Calendar calendar = Calendar.getInstance();

    int year = calendar.get(Calendar.YEAR);
    int month = calendar.get(Calendar.MONTH);
    int day = calendar.get(Calendar.DATE);

    System.out.print("�{���̓�����");
    System.out.println(year + "�N" + (month + 1) + "��" + day + "��");

    /* ���������j������J�n����Ă��邩�m�F���� */
    calendar.set(year, month, 1);
    int startWeek = calendar.get(Calendar.DAY_OF_WEEK);
    System.out.println("�����̗j����" + startWeek + "����");

    /* �挎�������܂ł����������m�F���� */
    calendar.set(year, month, 0);
    int beforeMonthlastDay = calendar.get(Calendar.DATE);
    System.out.println("�挎��" + beforeMonthlastDay + "���܂�");

    /* �����������܂ł����m�F���� */
    calendar.set(year, month + 1, 0);
    int thisMonthlastDay = calendar.get(Calendar.DATE);
    System.out.println("������" + thisMonthlastDay + "���܂�\r\n");

    int[] calendarDay = new int[42];
    int count = 0;

    /* �挎���̓��t���i�[���� */
    for (int i = startWeek - 2 ; i >= 0 ; i--){
      calendarDay[count++] = beforeMonthlastDay - i;
    }

    /* �������̓��t���i�[���� */
    for (int i = 1 ; i <= thisMonthlastDay ; i++){
      calendarDay[count++] = i;
    }

    /* �������̓��t���i�[���� */
    int nextMonthDay = 1;
    while (count % 7 != 0){
      calendarDay[count++] = nextMonthDay++;
    }

    int weekCount = count / 7;

    for (int i = 0 ; i < weekCount ; i++){
      for (int j = i * 7 ; j < i * 7 + 7 ; j++){
        if (calendarDay[j] < 10){
          System.out.print(" " + calendarDay[j] + " ");
        }else{
          System.out.print(calendarDay[j] + " ");
        }
      }

      System.out.print("\r\n");
    }
  }
}
