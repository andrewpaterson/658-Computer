package net.assembler.sixteenhigh.parser;

public class AssemblerTest
{
  public static void main(String[] args)
  {
    new SixteenHighParser("" +
                          "sub @sum_even_and_odd\n" +
                          "int16  i; int16 number; int16 even; int16 odd\n" +
                          "       number = 10; i = 0; even = 0; odd = 0\n" +
                          "bool   b\n" +
                          "loop:\n" +
                          "       b = i % 2\n" +
                          "       b ?\n" +
                          "       if= go even_\n" +
                          "       odd += i\n" +
                          "       go done\n" +
                          "even_:\n" +
                          "       even += i\n" +
                          "done:\n" +
                          "       i++; number ?- i\n" +
                          "       if> go loop\n" +
                          "       return");

  }
}
