package net.wdc65xx.wdc65816.addressingmode;

import net.wdc65xx.wdc65816.Cpu65816;
import net.wdc65xx.wdc65816.Executor;
import net.wdc65xx.wdc65816.WidthFromRegister;
import net.wdc65xx.wdc65816.interrupt.InterruptVector;

public class InstructionCycleFactory
{
  public static AbsoluteIndexedIndirectWithXJSRCycles createAbsoluteIndexedIndirectWithXJSRCycles()
  {
    return new AbsoluteIndexedIndirectWithXJSRCycles();
  }

  public static ImpliedXBACycles createImpliedXBACycles(Executor<Cpu65816> operation)
  {
    return new ImpliedXBACycles(operation);
  }

  public static AbsoluteIndirectJMLCycles createAbsoluteIndirectJMLCycles()
  {
    return new AbsoluteIndirectJMLCycles();
  }

  public static StopTheClockCycles createStopTheClockCycles(Executor<Cpu65816> operation)
  {
    return new StopTheClockCycles(operation);
  }

  public static StackPEICycles createStackPEICycles()
  {
    return new StackPEICycles();
  }

  public static WaitForInterruptCycles createWaitForInterruptCycles(Executor<Cpu65816> operation)
  {
    return new WaitForInterruptCycles(operation);
  }

  public static ImmediateREPSEPCycles createImmediateREPSEPCycles(Executor<Cpu65816> operation)
  {
    return new ImmediateREPSEPCycles(operation);
  }

  public static DirectIndexedWithYCycles createDirectIndexedWithYCycles(Executor<Cpu65816> operation, WidthFromRegister widthFromRegister)
  {
    return new DirectIndexedWithYCycles(operation, widthFromRegister);
  }

  public static StackPLBCycles createStackPLBCycles(Executor<Cpu65816> operation)
  {
    return new StackPLBCycles(operation);
  }

  public static AbsoluteLongIndexedWithXWriteCycles createAbsoluteLongIndexedWithXWriteCycles(Executor<Cpu65816> operation)
  {
    return new AbsoluteLongIndexedWithXWriteCycles(operation);
  }

  public static AbsoluteIndexedWithXWriteCycles createAbsoluteIndexedWithXWriteCycles(Executor<Cpu65816> operation)
  {
    return new AbsoluteIndexedWithXWriteCycles(operation);
  }

  public static AbsoluteIndexedWithYWriteCycles createAbsoluteIndexedWithYWriteCycles(Executor<Cpu65816> operation)
  {
    return new AbsoluteIndexedWithYWriteCycles(operation);
  }

  public static DirectIndirectLongIndexedWithYWriteCycles createDirectIndirectLongIndexedWithYWriteCycles(Executor<Cpu65816> operation)
  {
    return new DirectIndirectLongIndexedWithYWriteCycles(operation);
  }

  public static DirectIndexedWithYWriteCycles createDirectIndexedWithYWriteCycles(Executor<Cpu65816> operation, WidthFromRegister widthFromRegister)
  {
    return new DirectIndexedWithYWriteCycles(operation, widthFromRegister);
  }

  public static StackRelativeIndirectIndexedWithYWriteCycles createStackRelativeIndirectIndexedWithYWriteCycles(Executor<Cpu65816> operation)
  {
    return new StackRelativeIndirectIndexedWithYWriteCycles(operation);
  }

  public static DirectIndirectWriteCycles createDirectIndirectWriteCycles(Executor<Cpu65816> operation)
  {
    return new DirectIndirectWriteCycles(operation);
  }

  public static DirectIndirectIndexedWithYWriteCycles createDirectIndirectIndexedWithYWriteCycles(Executor<Cpu65816> operation)
  {
    return new DirectIndirectIndexedWithYWriteCycles(operation);
  }

  public static AbsoluteLongWriteCycles createAbsoluteLongWriteCycles(Executor<Cpu65816> operation)
  {
    return new AbsoluteLongWriteCycles(operation);
  }

  public static AbsoluteWriteCycles createAbsoluteWriteCycles(Executor<Cpu65816> operation, WidthFromRegister widthFromRegister)
  {
    return new AbsoluteWriteCycles(operation, widthFromRegister);
  }

  public static StackPHBCycles createStackPHBCycles(Executor<Cpu65816> operation)
  {
    return new StackPHBCycles(operation);
  }

  public static DirectIndirectLongWriteCycles createDirectIndirectLongWriteCycles(Executor<Cpu65816> operation)
  {
    return new DirectIndirectLongWriteCycles(operation);
  }

  public static StackRelativeWriteCycles createStackRelativeWriteCycles(Executor<Cpu65816> operation)
  {
    return new StackRelativeWriteCycles(operation);
  }

  public static RelativeLongCycles createRelativeLongCycles(Executor<Cpu65816> operation)
  {
    return new RelativeLongCycles(operation);
  }

  public static DirectIndexedIndirectWithXWriteCycles createDirectIndexedIndirectWithXWriteCycles(Executor<Cpu65816> operation)
  {
    return new DirectIndexedIndirectWithXWriteCycles(operation);
  }

  public static AbsoluteIndexedIndirectWithXJMPCycles createAbsoluteIndexedIndirectWithXJMPCycles()
  {
    return new AbsoluteIndexedIndirectWithXJMPCycles();
  }

  public static DirectIndexedWithXWriteCycles createDirectIndexedWithXWriteCycles(Executor<Cpu65816> operation, WidthFromRegister widthFromRegister)
  {
    return new DirectIndexedWithXWriteCycles(operation, widthFromRegister);
  }

  public static AbsoluteIndirectJMPCycles createAbsoluteIndirectJMPCycles()
  {
    return new AbsoluteIndirectJMPCycles();
  }

  public static StackRTLCycles createStackRTLCycles()
  {
    return new StackRTLCycles();
  }

  public static StackPullCycles createStackPullCycles(Executor<Cpu65816> operation, WidthFromRegister widthFromRegister)
  {
    return new StackPullCycles(operation, widthFromRegister);
  }

  public static DirectWriteCycles createDirectWriteCycles(Executor<Cpu65816> operation, WidthFromRegister widthFromRegister)
  {
    return new DirectWriteCycles(operation, widthFromRegister);
  }

  public static StackPERCycles createStackPERCycles(Executor<Cpu65816> operation)
  {
    return new StackPERCycles(operation);
  }

  public static StackRTSCycles createStackRTSCycles()
  {
    return new StackRTSCycles();
  }

  public static AbsoluteLongJMLCycles createAbsoluteLongJMLCycles()
  {
    return new AbsoluteLongJMLCycles();
  }

  public static AbsoluteJMPCycles createAbsoluteJMPCycles()
  {
    return new AbsoluteJMPCycles();
  }

  public static StackPHKCycles createStackPHKCycles(Executor<Cpu65816> operation)
  {
    return new StackPHKCycles(operation);
  }

  public static StackPushCycles createStackPushCycles(Executor<Cpu65816> operation, WidthFromRegister widthFromRegister)
  {
    return new StackPushCycles(operation, widthFromRegister);
  }

  public static BlockMoveCycles createBlockMoveCycles(Executor<Cpu65816> operation)
  {
    return new BlockMoveCycles(operation);
  }

  public static StackRTICycles createStackRTICycles(Executor<Cpu65816> operation)
  {
    return new StackRTICycles(operation);
  }

  public static StackPLDCycles createStackPLDCycles(Executor<Cpu65816> operation)
  {
    return new StackPLDCycles(operation);
  }

  public static StackPLPCycles createStackPLPCycles(Executor<Cpu65816> operation)
  {
    return new StackPLPCycles(operation);
  }

  public static AbsoluteLongJSLCycles createAbsoluteLongJSLCycles()
  {
    return new AbsoluteLongJSLCycles();
  }

  public static AbsoluteJSRCycles createAbsoluteJSRCycles()
  {
    return new AbsoluteJSRCycles();
  }

  public static AbsoluteLongIndexedWithXCycles createAbsoluteLongIndexedWithXCycles(Executor<Cpu65816> operation)
  {
    return new AbsoluteLongIndexedWithXCycles(operation);
  }

  public static AbsoluteIndexedWithXRMWCycles createAbsoluteIndexedWithXRMWCycles(Executor<Cpu65816> operation)
  {
    return new AbsoluteIndexedWithXRMWCycles(operation);
  }

  public static AbsoluteIndexedWithXCycles createAbsoluteIndexedWithXCycles(Executor<Cpu65816> operation, WidthFromRegister widthFromRegister)
  {
    return new AbsoluteIndexedWithXCycles(operation, widthFromRegister);
  }

  public static AbsoluteIndexedWithYCycles createAbsoluteIndexedWithYCycles(Executor<Cpu65816> operation, WidthFromRegister widthFromRegister)
  {
    return new AbsoluteIndexedWithYCycles(operation, widthFromRegister);
  }

  public static ImpliedCycles createImpliedCycles(Executor<Cpu65816> operation)
  {
    return new ImpliedCycles(operation);
  }

  public static DirectIndirectLongIndexedWithYCycles createDirectIndirectLongIndexedWithYCycles(Executor<Cpu65816> operation)
  {
    return new DirectIndirectLongIndexedWithYCycles(operation);
  }

  public static DirectIndexedWithXRMWCycles createDirectIndexedWithXRMWCycles(Executor<Cpu65816> operation)
  {
    return new DirectIndexedWithXRMWCycles(operation);
  }

  public static DirectIndexedWithXCycles createDirectIndexedWithXCycles(Executor<Cpu65816> operation, WidthFromRegister widthFromRegister)
  {
    return new DirectIndexedWithXCycles(operation, widthFromRegister);
  }

  public static StackRelativeIndirectIndexedWithYCycles createStackRelativeIndirectIndexedWithYCycles(Executor<Cpu65816> operation)
  {
    return new StackRelativeIndirectIndexedWithYCycles(operation);
  }

  public static DirectIndirectCycles createDirectIndirectCycles(Executor<Cpu65816> operation)
  {
    return new DirectIndirectCycles(operation);
  }

  public static DirectIndirectIndexedWithYCycles createDirectIndirectIndexedWithYCycles(Executor<Cpu65816> operation)
  {
    return new DirectIndirectIndexedWithYCycles(operation);
  }

  public static RelativeShortCycles createRelativeShortCycles(Executor<Cpu65816> operation)
  {
    return new RelativeShortCycles(operation);
  }

  public static AbsoluteLongCycles createAbsoluteLongCycles(Executor<Cpu65816> operation)
  {
    return new AbsoluteLongCycles(operation);
  }

  public static AbsoluteCycles createAbsoluteCycles(Executor<Cpu65816> operation, WidthFromRegister widthFromRegister)
  {
    return new AbsoluteCycles(operation, widthFromRegister);
  }

  public static AbsoluteRMWCycles createAbsoluteRMWCycles(Executor<Cpu65816> operation)
  {
    return new AbsoluteRMWCycles(operation);
  }

  public static StackImpliedPHDCycles createStackImpliedPHDCycles(Executor<Cpu65816> operation)
  {
    return new StackImpliedPHDCycles(operation);
  }

  public static AccumulatorCycles createAccumulatorCycles(Executor<Cpu65816> operation)
  {
    return new AccumulatorCycles(operation);
  }

  public static ImmediateCycles createImmediateCycles(Executor<Cpu65816> operation, WidthFromRegister widthFromRegister)
  {
    return new ImmediateCycles(operation, widthFromRegister);
  }

  public static StackImpliedPHPCycles createStackImpliedPHPCycles(Executor<Cpu65816> operation)
  {
    return new StackImpliedPHPCycles(operation);
  }

  public static DirectIndirectLongCycles createDirectIndirectLongCycles(Executor<Cpu65816> operation)
  {
    return new DirectIndirectLongCycles(operation);
  }

  public static DirectCycles createDirectCycles(Executor<Cpu65816> operation, WidthFromRegister widthFromRegister)
  {
    return new DirectCycles(operation, widthFromRegister);
  }

  public static DirectRMWCycles createDirectRMWCycles(Executor<Cpu65816> operation)
  {
    return new DirectRMWCycles(operation);
  }

  public static StackRelativeCycles createStackRelativeCycles(Executor<Cpu65816> operation)
  {
    return new StackRelativeCycles(operation);
  }

  public static DirectIndexedIndirectWithXCycles createDirectIndexedIndirectWithXCycles(Executor<Cpu65816> operation)
  {
    return new DirectIndexedIndirectWithXCycles(operation);
  }

  public static StackSoftwareInterruptCycles createStackSoftwareInterruptCycles(InterruptVector interruptVector, Executor<Cpu65816> operation)
  {
    return new StackSoftwareInterruptCycles(interruptVector, operation);
  }

  public static StackPEACycles createStackPEACycles()
  {
    return new StackPEACycles();
  }

  public static FetchOpCodeCycles createFetchOpCodeCycles()
  {
    return new FetchOpCodeCycles();
  }

  public static StackHardwareInterruptCycles createStackHardwareInterruptCycles(InterruptVector interruptVector, Executor<Cpu65816> operation)
  {
    return new StackHardwareInterruptCycles(interruptVector, operation);
  }

  public static StackResetCycles createStackResetCycles(InterruptVector interruptVector, Executor<Cpu65816> operation)
  {
    return new StackResetCycles(interruptVector, operation);
  }

}

