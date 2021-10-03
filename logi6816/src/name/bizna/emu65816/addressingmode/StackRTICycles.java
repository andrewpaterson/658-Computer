package name.bizna.emu65816.addressingmode;

import name.bizna.emu65816.Cpu65816;

import java.util.function.Consumer;

import static name.bizna.emu65816.AddressingMode.Stack;

public class StackRTICycles
    extends InstructionCycles
{
  //22g
  public StackRTICycles(Consumer<Cpu65816> operation)
  {
    super(Stack,
          new BusCycle(Address(PBR(), PC()), OpCode(), PC_inc()),
          new BusCycle(Address(PBR(), PC()), IO(), PC_inc()),
          new BusCycle(Address(PBR(), PC()), IO(), PC_inc(), SP_inc()),
          new BusCycle(Address(S()), Read_DataLow(), SP_inc()),  //Processor status
          new BusCycle(Address(S()), Read_NewPCL(), SP_inc()),
          new BusCycle(Address(S()), Read_NewPCH(), SP_inc()),
          new BusCycle(Address(S()), Read_NewPBR(), E(operation), PC_e(New_PBR(), New_PC()), DONE()));  //Set processor status from data low.
  }
}

