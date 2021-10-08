package name.bizna.cpu.addressingmode;

import name.bizna.cpu.Cpu65816;
import name.bizna.cpu.WidthFromRegister;

import java.util.function.Consumer;

import static name.bizna.cpu.AddressingMode.DirectIndexedWithY;

public class DirectIndexedWithYWriteCycles
    extends InstructionCycles
{
  //17
  public DirectIndexedWithYWriteCycles(Consumer<Cpu65816> operation, WidthFromRegister width)
  {
    super(DirectIndexedWithY,
          new BusCycle(Address(PBR(), PC()), OpCode(), PC_inc()),
          new BusCycle(Address(PBR(), PC()), Read_D0(), PC_inc()),
          new BusCycle(Address(PBR(), PC()), IO(), new NoteTwo()),
          new BusCycle(Address(PBR(), PC()), IO()),
          new BusCycle(Address(DP(), D0(), Y()), E(operation), Write_DataLow(), DONE8Bit(width)),
          new BusCycle(Address(DP(), D0(), Y(), o(1)), Write_DataHigh(), DONE16Bit(width)));
  }
}

