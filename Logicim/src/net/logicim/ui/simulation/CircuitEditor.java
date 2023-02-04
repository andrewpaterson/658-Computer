package net.logicim.ui.simulation;

import net.logicim.common.SimulatorException;
import net.logicim.common.geometry.Line;
import net.logicim.common.geometry.LineMinimiser;
import net.logicim.common.geometry.LinePositionCache;
import net.logicim.common.geometry.LineSplitter;
import net.logicim.common.type.Float2D;
import net.logicim.common.type.Int2D;
import net.logicim.common.type.Positions;
import net.logicim.data.circuit.CircuitData;
import net.logicim.data.circuit.TimelineData;
import net.logicim.data.integratedcircuit.common.StaticData;
import net.logicim.data.wire.TraceData;
import net.logicim.data.wire.TraceLoader;
import net.logicim.domain.Simulation;
import net.logicim.domain.common.Circuit;
import net.logicim.domain.common.Component;
import net.logicim.domain.common.IntegratedCircuit;
import net.logicim.domain.common.Timeline;
import net.logicim.domain.passive.common.Passive;
import net.logicim.ui.SelectionRectangle;
import net.logicim.ui.common.*;
import net.logicim.ui.common.integratedcircuit.*;
import net.logicim.ui.common.port.PortView;
import net.logicim.ui.common.wire.TraceView;
import net.logicim.ui.common.wire.TunnelView;
import net.logicim.ui.common.wire.WireView;
import net.logicim.ui.connection.LocalConnectionNet;
import net.logicim.ui.connection.PortTraceFinder;
import net.logicim.ui.shape.common.BoundingBox;
import net.logicim.ui.simulation.component.decorative.common.DecorativeView;

import java.awt.*;
import java.util.List;
import java.util.*;

public class CircuitEditor
{
  protected Set<IntegratedCircuitView<?, ?>> integratedCircuitViews;
  protected Set<PassiveView<?, ?>> passiveViews;
  protected Set<DecorativeView<?>> decorativeViews;

  protected Set<TraceView> traceViews;
  protected Set<TunnelView> tunnelViews;
  protected Map<String, Set<TunnelView>> tunnelViewsMap;

  protected Circuit circuit;
  protected Simulation simulation;
  protected List<View> selection;

  protected ConnectionViewCache connectionViewCache;

  public CircuitEditor()
  {
    this.circuit = new Circuit();
    this.simulation = circuit.resetSimulation();
    this.integratedCircuitViews = new LinkedHashSet<>();
    this.traceViews = new LinkedHashSet<>();
    this.tunnelViewsMap = new LinkedHashMap<>();
    this.passiveViews = new LinkedHashSet<>();
    this.tunnelViews = new LinkedHashSet<>();
    this.decorativeViews = new LinkedHashSet<>();
    this.selection = new ArrayList<>();
    this.connectionViewCache = new ConnectionViewCache();
  }

  public void paint(Graphics2D graphics, Viewport viewport)
  {
    long time = getTime();
    List<View> views;
    synchronized (this)
    {
      views = getAllViews();
    }

    for (View view : views)
    {
      view.paint(graphics, viewport, time);
    }
  }

  protected List<View> getAllViews()
  {
    List<View> views = new ArrayList<>(traceViews);
    views.addAll(tunnelViews);
    views.addAll(decorativeViews);
    views.addAll(passiveViews);
    views.addAll(integratedCircuitViews);
    return views;
  }

  protected long getTime()
  {
    return simulation.getTime();
  }

  public Circuit getCircuit()
  {
    return circuit;
  }

  public void deleteComponentView(StaticView<?> componentView)
  {
    Set<PortView> updatedPortViews;
    if (componentView instanceof IntegratedCircuitView)
    {
      updatedPortViews = deleteIntegratedCircuit((IntegratedCircuitView<?, ?>) componentView);
    }
    else if (componentView instanceof PassiveView)
    {
      updatedPortViews = deletePassiveView((PassiveView<?, ?>) componentView);
    }
    else if (componentView instanceof DecorativeView)
    {
      updatedPortViews = deleteDecorativeView((DecorativeView<?>) componentView);
    }
    else if (componentView instanceof TunnelView)
    {
      updatedPortViews = deleteTunnelView((TunnelView) componentView);
    }
    else if (componentView == null)
    {
      throw new SimulatorException("Cannot delete null view.");
    }
    else
    {
      throw new SimulatorException("Cannot delete view of class [%s].", componentView.getClass().getSimpleName());
    }

    fireConnectionEvents(updatedPortViews);

    validateConsistency();
  }

  public Set<PortView> deleteTunnelView(TunnelView tunnelView)
  {
    Set<ConnectionView> connectionViews = tunnelView.getAllConnectedTunnelConnections();
    removeTunnelView(tunnelView);

    LinkedHashSet<PortView> updatedPortViews = new LinkedHashSet<>();
    for (ConnectionView connectionView : connectionViews)
    {
      updatedPortViews.addAll(connectNewConnections(connectionView));
    }
    return updatedPortViews;
  }

  protected Set<PortView> deleteIntegratedCircuit(IntegratedCircuitView<?, ?> integratedCircuitView)
  {
    List<ConnectionView> connectionViews = disconnectView(integratedCircuitView);

    IntegratedCircuit<?, ?> integratedCircuit = integratedCircuitView.getIntegratedCircuit();
    circuit.remove(integratedCircuit);
    removeIntegratedCircuitView(integratedCircuitView);

    return connectConnections(connectionViews);
  }

  protected Set<PortView> deletePassiveView(PassiveView<?, ?> passiveView)
  {
    List<ConnectionView> connectionViews = disconnectView(passiveView);

    Passive powerSource = passiveView.getComponent();
    circuit.remove(powerSource);
    removePassiveView(passiveView);

    return connectConnections(connectionViews);
  }

  protected Set<PortView> deleteDecorativeView(DecorativeView<?> decorativeView)
  {
    List<ConnectionView> connectionViews = disconnectView(decorativeView);

    removeDecorativeView(decorativeView);

    return connectConnections(connectionViews);
  }

  protected List<ConnectionView> disconnectView(View view)
  {
    if (view == null)
    {
      throw new SimulatorException("Cannot disconnect [null] view.");
    }

    List<ConnectionView> connectionViews = view.getConnections();
    if (connectionViews == null)
    {
      throw new SimulatorException("Cannot disconnect %s with [null] connections.", view.toIdentifierString());
    }

    if (!(view instanceof WireView))
    {
      connectionViewCache.removeAll(view, connectionViews);
      for (ConnectionView connectionView : connectionViews)
      {
        view.disconnect(simulation, connectionView);
      }
    }
    else
    {
      connectionViewCache.removeAll(view, connectionViews);
      ((WireView) view).disconnect(simulation);
    }

    Set<ConnectionView> updatedConnectionViews = new LinkedHashSet<>();
    for (ConnectionView connectionView : connectionViews)
    {
      if (!updatedConnectionViews.contains(connectionView))
      {
        List<LocalConnectionNet> connectionNets = PortTraceFinder.findAndConnectTraces(simulation, connectionView);
        updatedConnectionViews.addAll(PortTraceFinder.getConnectionViews(connectionNets));
      }
    }

    Component component = view.getComponent();
    if (component != null)
    {
      circuit.disconnectComponent(component, simulation);
    }
    return connectionViews;
  }

  public Set<PortView> connectConnections(List<ConnectionView> connectionViews)
  {
    Set<PortView> updatedPortViews = new LinkedHashSet<>();
    for (ConnectionView connectionView : connectionViews)
    {
      updatedPortViews.addAll(connectNewConnections(connectionView));
    }

    return updatedPortViews;
  }

  public boolean deleteTraceViews(ConnectionView connectionView)
  {
    Set<PortView> updatedPortViews = new LinkedHashSet<>();
    List<View> connectedComponents = new ArrayList<>(connectionView.getConnectedComponents());
    boolean traceDeleted = false;
    for (View view : connectedComponents)
    {
      if (view instanceof TraceView)
      {
        traceDeleted = true;
        Set<PortView> localUpdatedPortViews = deleteTraceView((TraceView) view);
        updatedPortViews.addAll(localUpdatedPortViews);
      }
    }

    if (traceDeleted)
    {
      fireConnectionEvents(updatedPortViews);
      validateConsistency();
    }

    return traceDeleted;
  }

  public void editActionDeleteTraceView(ConnectionView connectionView, TraceView traceView)
  {
    Set<PortView> updatedPortViews = new LinkedHashSet<>();
    if (!connectionView.isConcrete())
    {
      updatedPortViews = deleteTraceView(traceView);
    }
    else
    {
      List<View> connectedComponents = new ArrayList<>(connectionView.getConnectedComponents());
      for (View view : connectedComponents)
      {
        if (view instanceof TraceView)
        {
          Set<PortView> portViews = deleteTraceView((TraceView) view);
          updatedPortViews.addAll(portViews);
        }
      }
    }

    fireConnectionEvents(updatedPortViews);
    validateConsistency();
  }

  public Set<PortView> connectNewConnections(ConnectionView connectionView)
  {
    List<LocalConnectionNet> connectionNets = PortTraceFinder.findAndConnectTraces(simulation, connectionView);
    return new LinkedHashSet<>(PortTraceFinder.getPortViews(connectionNets));
  }

  public Simulation reset()
  {
    simulation = circuit.resetSimulation();
    return simulation;
  }

  public void runSimultaneous()
  {
    simulation.runSimultaneous();
  }

  public void runToTime(long timeForward)
  {
    simulation.runToTime(timeForward);
  }

  public StaticView<?> getComponentViewInScreenSpace(Viewport viewport, Int2D screenPosition)
  {
    List<StaticView<?>> selectedViews = getComponentViewsInScreenSpace(viewport, screenPosition);

    if (selectedViews.size() == 1)
    {
      return selectedViews.get(0);
    }
    else if (selectedViews.size() == 0)
    {
      return null;
    }
    else
    {
      Int2D boundBoxPosition = new Int2D();
      Int2D boundBoxDimension = new Int2D();
      float shortestDistance = Float.MAX_VALUE;
      StaticView<?> closestView = null;
      for (StaticView<?> view : selectedViews)
      {
        if (view.getBoundingBoxInScreenSpace(viewport, boundBoxPosition, boundBoxDimension))
        {
          boundBoxPosition.add(boundBoxDimension.x / 2, boundBoxDimension.y / 2);
          float distance = BoundingBox.calculateDistance(screenPosition, boundBoxPosition);
          if (distance < shortestDistance)
          {
            closestView = view;
            shortestDistance = distance;
          }
        }
      }
      return closestView;
    }
  }

  public StaticViewIterator staticViewIterator()
  {
    return new StaticViewIterator(tunnelViews, integratedCircuitViews, passiveViews, decorativeViews);
  }

  public List<StaticView<?>> getComponentViewsInScreenSpace(Viewport viewport, Int2D screenPosition)
  {
    List<StaticView<?>> selectedViews = new ArrayList<>();
    StaticViewIterator iterator = staticViewIterator();
    while (iterator.hasNext())
    {
      StaticView<?> view = iterator.next();
      if (isInScreenSpaceBoundingBox(viewport, screenPosition, view))
      {
        selectedViews.add(view);
      }
    }
    return selectedViews;
  }

  protected boolean isInScreenSpaceBoundingBox(Viewport viewport, Int2D screenPosition, StaticView<?> view)
  {
    if (view.isEnabled())
    {
      Int2D boundBoxPosition = new Int2D();
      Int2D boundBoxDimension = new Int2D();
      view.getBoundingBoxInScreenSpace(viewport, boundBoxPosition, boundBoxDimension);
      if (BoundingBox.containsPoint(screenPosition, boundBoxPosition, boundBoxDimension))
      {
        return true;
      }
    }
    return false;
  }

  public List<TraceView> getTraceViewsInScreenSpace(Viewport viewport, Int2D screenPosition)
  {
    int x = viewport.transformScreenToGridX(screenPosition.x);
    int y = viewport.transformScreenToGridY(screenPosition.y);
    List<TraceView> traceViews = new ArrayList<>();
    for (TraceView traceView : this.traceViews)
    {
      if (traceView.contains(x, y))
      {
        traceViews.add(traceView);
      }
    }
    return traceViews;
  }

  public TraceView getTraceViewInScreenSpace(Viewport viewport, Int2D screenPosition)
  {
    List<TraceView> selectedViews = getTraceViewsInScreenSpace(viewport, screenPosition);

    if (selectedViews.size() == 1)
    {
      return selectedViews.get(0);
    }
    else if (selectedViews.size() == 0)
    {
      return null;
    }
    else
    {
      Int2D center = new Int2D();
      float shortestDistance = Float.MAX_VALUE;
      TraceView closestView = null;
      for (TraceView view : selectedViews)
      {
        view.getCenter(center);
        float distance = BoundingBox.calculateDistance(screenPosition, center);
        if (distance < shortestDistance)
        {
          closestView = view;
          shortestDistance = distance;
        }
      }
      return closestView;
    }
  }

  public List<TraceView> getTraceViewsInGridSpace(Int2D position)
  {
    return getTraceViewsInGridSpace(position.x, position.y);
  }

  public List<TraceView> getTraceViewsInGridSpace(int x, int y)
  {
    List<TraceView> result = new ArrayList<>();
    for (TraceView traceView : traceViews)
    {
      if (traceView.contains(x, y))
      {
        result.add(traceView);
      }
    }
    return result;
  }

  public List<TraceOverlap> getTracesOverlapping(Line line)
  {
    List<TraceOverlap> overlaps = new ArrayList<>();
    for (TraceView traceView : traceViews)
    {
      LineOverlap overlap = traceView.getOverlap(line, false);
      if (overlap != null)
      {
        overlaps.add(new TraceOverlap(overlap, traceView));
      }
    }
    return overlaps;
  }

  public ConnectionView getOrAddConnection(Int2D position, View view)
  {
    return connectionViewCache.getOrAddConnection(position, view);
  }

  public TraceView mergeTrace(TraceView traceView)
  {
    Rotation direction = traceView.getDirection();
    ConnectionView startConnection = traceView.getStartConnection();
    ConnectionView endConnection = traceView.getEndConnection();

    List<TraceView> towardsEnd = findStraightTracesInDirection(traceView, direction, endConnection);
    List<TraceView> towardsStart = findStraightTracesInDirection(traceView, direction.opposite(), startConnection);
    if (towardsEnd.size() > 0 | (towardsStart.size() > 0))
    {
      List<TraceView> mergeViews = new ArrayList<>(towardsStart);
      mergeViews.add(traceView);
      mergeViews.addAll(towardsEnd);
      Int2D smallest = new Int2D(traceView.getMinimumX(), traceView.getMinimumY());
      Int2D largest = new Int2D(traceView.getMaximumX(), traceView.getMaximumY());
      for (TraceView mergeView : mergeViews)
      {
        int x1 = mergeView.getMinimumX();
        if (x1 < smallest.x)
        {
          smallest.x = x1;
        }
        int y1 = mergeView.getMinimumY();
        if (y1 < smallest.y)
        {
          smallest.y = y1;
        }
        int x2 = mergeView.getMaximumX();
        if (x2 > largest.x)
        {
          largest.x = x2;
        }
        int y2 = mergeView.getMaximumY();
        if (y2 > largest.y)
        {
          largest.y = y2;
        }
        disconnectView(traceView);
        removeTraceView(mergeView);
      }

      if (isValidTrace(smallest, largest))
      {
        return new TraceView(this, smallest, largest);
      }
      else
      {
        throw new SimulatorException("Invalid trace created from merged traces.");
      }
    }
    else
    {
      return traceView;
    }
  }

  private boolean isValidTrace(Int2D start, Int2D end)
  {
    if ((start != null) && (end != null))
    {
      if (((start.x == end.x) || (start.y == end.y)) &&
          !((start.x == end.x) && (start.y == end.y)))
      {
        return true;
      }
    }
    return false;
  }

  private List<TraceView> findStraightTracesInDirection(TraceView traceView, Rotation direction, ConnectionView connectionView)
  {
    if (connectionView == null)
    {
      throw new SimulatorException("End connection may not be null.");
    }
    ArrayList<TraceView> result = new ArrayList<>();

    TraceView currentTrace = traceView;
    for (int i = 0; ; i++)
    {
      if (connectionView == null)
      {
        throw new SimulatorException("Cannot find traces from a null connection.  Iteration [%s].", i);
      }

      List<View> connectedComponents = connectionView.getConnectedComponents();
      TraceView otherTrace = currentTrace.getOtherTraceView(connectedComponents);

      if (otherTrace != null)
      {
        Rotation otherDirection = otherTrace.getDirection();
        if (direction.isStraight(otherDirection))
        {
          result.add(otherTrace);
          connectionView = otherTrace.getOpposite(connectionView);
          currentTrace = otherTrace;
        }
        else
        {
          break;
        }
      }
      else
      {
        break;
      }
    }

    return result;
  }

  private List<TraceView> splitTrace(Int2D position, Rotation direction)
  {
    List<TraceView> result = new ArrayList<>();
    List<TraceView> traceViews = getTraceViewsInGridSpace(position);
    for (TraceView traceView : traceViews)
    {
      ConnectionView connectionView = traceView.getPotentialConnectionsInGrid(position);
      if (connectionView instanceof HoverConnectionView)
      {
        if (!direction.isStraight(traceView.getDirection()))
        {
          Int2D startPosition = traceView.getStartPosition();
          Int2D endPosition = traceView.getEndPosition();

          disconnectView(traceView);
          removeTraceView(traceView);

          if (!position.equals(startPosition))
          {
            result.add(new TraceView(this, startPosition, position));
          }
          if (!position.equals(endPosition))
          {
            result.add(new TraceView(this, position, endPosition));
          }
        }
      }
    }
    return result;
  }

  public Set<PortView> deleteTraceView(TraceView traceView)
  {
    if (!traceView.hasNoConnections())
    {
      ConnectionView startConnection = traceView.getStartConnection();
      ConnectionView endConnection = traceView.getEndConnection();

      disconnectView(traceView);
      removeTraceView(traceView);

      Set<PortView> updatedPortViews = mergeAndConnectTracesAfterDelete(startConnection);
      updatedPortViews.addAll(mergeAndConnectTracesAfterDelete(endConnection));
      return updatedPortViews;
    }
    else
    {
      removeTraceView(traceView);
      return new LinkedHashSet<>();
    }
  }

  protected Set<PortView> mergeAndConnectTracesAfterDelete(ConnectionView connectionView)
  {
    TraceView traceView = mergeTraceConnectionForDelete(connectionView);
    ConnectionView connection;
    if (traceView != null)
    {
      connection = traceView.getStartConnection();
    }
    else
    {
      connection = connectionView;
    }
    return connectNewConnections(connection);
  }

  private TraceView mergeTraceConnectionForDelete(ConnectionView startConnection)
  {
    List<View> connectedComponents = startConnection.getConnectedComponents();
    TraceView traceView1 = null;
    TraceView traceView2 = null;
    if (connectedComponents.size() == 2)
    {
      for (View connectedComponent : connectedComponents)
      {
        if (connectedComponent instanceof TraceView)
        {
          if (traceView1 == null)
          {
            traceView1 = (TraceView) connectedComponent;
          }
          else
          {
            traceView2 = (TraceView) connectedComponent;
          }
        }
      }
    }

    if ((traceView1 != null) && (traceView2 != null))
    {
      if (!traceView1.hasNoConnections())
      {
        return mergeTrace(traceView1);
      }
    }
    return null;
  }

  public Set<TraceView> createTraceViews(Line line)
  {
    List<TraceView> editedTraceViews = new ArrayList<>();

    editedTraceViews.addAll(splitTrace(line.getStart(), line.getDirection()));
    editedTraceViews.addAll(splitTrace(line.getEnd(), line.getDirection()));

    List<ConnectionView> sortedConnections = new ArrayList<>(getConnectionsOnLine(line));

    Collections.sort(sortedConnections);

    if (sortedConnections.size() == 0)
    {
      editedTraceViews.add(new TraceView(this, line.getStart(), line.getEnd()));
    }
    else
    {
      if (line.isEastWest())
      {
        List<Int2D> positions = getEastWestPositions(line, sortedConnections);
        editedTraceViews.addAll(createTracesBetweenEmptyPositions(positions));
      }
      else if (line.isNorthSouth())
      {
        List<Int2D> positions = getNorthSouthPositions(line, sortedConnections);
        editedTraceViews.addAll(createTracesBetweenEmptyPositions(positions));
      }
    }

    Set<TraceView> mergedTraces = new LinkedHashSet<>();
    for (TraceView traceView : editedTraceViews)
    {
      if (!traceView.hasNoConnections())
      {
        TraceView mergedTrace = mergeTrace(traceView);
        if (!mergedTrace.hasNoConnections())
        {
          mergedTraces.add(mergedTrace);
        }
      }
    }
    return mergedTraces;
  }

  private List<TraceView> createTracesBetweenEmptyPositions(List<Int2D> positions)
  {
    List<TraceView> result = new ArrayList<>();
    for (int i = 0; i < positions.size() - 1; i++)
    {
      Int2D start = positions.get(i);
      Int2D end = positions.get(i + 1);
      List<TraceOverlap> tracesOverlapping = getTracesOverlapping(new Line(start, end));
      if (tracesOverlapping.size() == 0)
      {
        TraceView traceView = new TraceView(this, start, end);
        result.add(traceView);
      }
    }
    return result;
  }

  private List<Int2D> getEastWestPositions(Line line, List<ConnectionView> sortedConnections)
  {
    int minimumX = line.getMinimumX();
    int maximumX = line.getMaximumX();
    int y = line.getY();

    Int2D start = new Int2D(minimumX, y);
    Int2D end = new Int2D(maximumX, y);
    return getPositions(sortedConnections, start, end);
  }

  private List<Int2D> getNorthSouthPositions(Line line, List<ConnectionView> sortedConnections)
  {
    int minimumY = line.getMinimumY();
    int maximumY = line.getMaximumY();
    int x = line.getX();

    Int2D start = new Int2D(x, minimumY);
    Int2D end = new Int2D(x, maximumY);
    return getPositions(sortedConnections, start, end);
  }

  private List<Int2D> getPositions(List<ConnectionView> sortedConnections, Int2D start, Int2D end)
  {
    List<Int2D> positions = new ArrayList<>();

    ConnectionView firstConnection = sortedConnections.get(0);
    if (!firstConnection.getGridPosition().equals(start))
    {
      positions.add(start);
    }

    for (ConnectionView sortedConnection : sortedConnections)
    {
      positions.add(sortedConnection.getGridPosition());
    }

    Int2D lastPosition = positions.get(positions.size() - 1);
    if (!end.equals(lastPosition))
    {
      positions.add(end);
    }
    return positions;
  }

  private Set<ConnectionView> getConnectionsOnLine(Line line)
  {
    Set<ConnectionView> notStraightConnections = new LinkedHashSet<>();
    notStraightConnections.addAll(getComponentPortConnectionsOnLine(line));
    notStraightConnections.addAll(getTraceConnectionsOnLine(line));
    return notStraightConnections;
  }

  private Set<ConnectionView> getComponentPortConnectionsOnLine(Line line)
  {
    Set<ConnectionView> connectionViews = new LinkedHashSet<>();
    StaticViewIterator staticViewIterator = staticViewIterator();
    while (staticViewIterator.hasNext())
    {
      StaticView<?> staticView = staticViewIterator.next();
      List<ConnectionView> connections = staticView.getConnections();
      for (ConnectionView connectionView : connections)
      {
        if (line.isPositionOn(connectionView.getGridPosition()))
        {
          connectionViews.add(connectionView);
        }
      }
    }

    return connectionViews;
  }

  private Set<ConnectionView> getTraceConnectionsOnLine(Line line)
  {
    Set<ConnectionView> notStraightConnections = new LinkedHashSet<>();
    for (TraceView traceView : traceViews)
    {
      if (line.isPositionOn(traceView.getStartPosition()))
      {
        notStraightConnections.add(traceView.getStartConnection());
      }
      if (line.isPositionOn(traceView.getEndPosition()))
      {
        notStraightConnections.add(traceView.getEndConnection());
      }
    }
    return notStraightConnections;
  }

  public void validateConsistency()
  {
    validateConnectionViews();
    validateTracesContainOnlyCurrentViews();
    validateTunnelViews();
  }

  private void validateConnectionViews()
  {
    StaticViewIterator iterator = staticViewIterator();
    while (iterator.hasNext())
    {
      StaticView<?> staticView = iterator.next();
      if (staticView instanceof ComponentView)
      {
        ComponentView<?> componentView = (ComponentView<?>) staticView;
        List<PortView> portViews = componentView.getPorts();
        for (PortView portView : portViews)
        {
          Int2D portPosition = portView.getGridPosition();
          ConnectionView connectionView = portView.getConnection();
          if (connectionView != null)
          {
            Int2D connectionPosition = connectionView.getGridPosition();
            if (connectionPosition == null)
            {
              throw new SimulatorException("Position on connection on %s cannot be null.", componentView.toIdentifierString());
            }
            if (!portPosition.equals(connectionPosition))
            {
              throw new SimulatorException("%s port [%s] position (%s) must be equal to connection position (%s).",
                                           componentView.toIdentifierString(),
                                           portView.getText(),
                                           Int2D.toString(portPosition),
                                           Int2D.toString(connectionPosition));
            }
          }
        }
      }

      List<ConnectionView> localConnectionViews = staticView.getConnections();
      for (ConnectionView connectionView : localConnectionViews)
      {
        if (connectionView == null)
        {
          throw new SimulatorException("Connection on %s cannot be null.", staticView.toIdentifierString());
        }
        Int2D connectionPosition = connectionView.getGridPosition();
        if (connectionPosition == null)
        {
          throw new SimulatorException("Position on connection on %s cannot be null.", staticView.toIdentifierString());
        }

        ConnectionView cacheConnectionView = connectionViewCache.getConnection(connectionPosition);
        if (cacheConnectionView != connectionView)
        {
          throw new SimulatorException("%s connection (%s) must be equal to cache connection (%s).",
                                       staticView.toIdentifierString(),
                                       Int2D.toString(connectionPosition),
                                       ConnectionView.toPositionString(cacheConnectionView));
        }
      }
    }

    connectionViewCache.validatePositions();
  }

  protected void validateTunnelViews()
  {
    for (TunnelView tunnelView : tunnelViews)
    {
      if (!tunnelView.isRemoved())
      {
        String name = tunnelView.getName();
        String sanitisedName = name.trim().toLowerCase();
        if (!tunnelView.getSanitisedName().equals(sanitisedName))
        {
          throw new SimulatorException("Tunnel view sanitised name is [%s] but should be [%s].", tunnelView.getSanitisedName(), sanitisedName);
        }

        Set<TunnelView> tunnelViews = tunnelViewsMap.get(sanitisedName);
        if (!sanitisedName.isEmpty())
        {
          if (tunnelViews == null)
          {
            throw new SimulatorException("TunnelViewsMap did not contain a map for name [%s]", tunnelView.getName());
          }
          if (!tunnelViews.contains(tunnelView))
          {
            throw new SimulatorException("TunnelViewsMap did not contain tunnel view [%s].", tunnelView.getName());
          }
        }
      }
    }

    for (String sanitisedName : tunnelViewsMap.keySet())
    {
      Set<TunnelView> tunnelViews = tunnelViewsMap.get(sanitisedName);
      for (TunnelView tunnelView : tunnelViews)
      {
        if (!this.tunnelViews.contains(tunnelView))
        {
          throw new SimulatorException("TunnelViews did not contain tunnel view [%s].", tunnelView.getName());
        }
      }
    }
  }

  protected void validateTracesContainOnlyCurrentViews()
  {
    for (TraceView traceView : traceViews)
    {
      if (!traceView.hasNoConnections())
      {
        ConnectionView startConnection = traceView.getStartConnection();
        for (View view : startConnection.getConnectedComponents())
        {
          boolean contained = true;
          if (view instanceof IntegratedCircuitView)
          {
            if (!integratedCircuitViews.contains(view))
            {
              contained = false;
            }
          }
          else if (view instanceof TraceView)
          {
            if (!traceViews.contains(view))
            {
              contained = false;
            }
          }
          else if (view instanceof PassiveView)
          {
            if (!passiveViews.contains(view))
            {
              contained = false;
            }
          }
          else if (view instanceof DecorativeView)
          {
            if (!decorativeViews.contains(view))
            {
              contained = false;
            }
          }
          else if (view instanceof TunnelView)
          {
            if (!tunnelViews.contains(view))
            {
              contained = false;
            }
          }
          else if (view == null)
          {
            throw new SimulatorException("TraceView [" + traceView.getDescription() + "] does not include trace has null connection.");
          }
          else
          {
            throw new SimulatorException(view.getDescription() + " referenced by TraceView [" + traceView.getDescription() + "] has not been included in validateConsistency.");
          }

          if (!contained)
          {
            throw new SimulatorException(view.getDescription() + " referenced by TraceView [" + traceView.getDescription() + "] does not include trace.");
          }
        }
      }
    }
  }

  public CircuitData save()
  {
    Set<View> selection = new HashSet<>(this.selection);
    ArrayList<StaticData<?>> componentDatas = new ArrayList<>();
    StaticViewIterator iterator = staticViewIterator();
    while (iterator.hasNext())
    {
      StaticView<?> staticView = iterator.next();
      StaticData<?> staticData = (StaticData<?>) staticView.save(selection.contains(staticView));
      if (staticData == null)
      {
        throw new SimulatorException("%s save may not return null.", staticView.toIdentifierString());
      }

      componentDatas.add(staticData);
    }

    ArrayList<TraceData> traceDatas = new ArrayList<>();
    for (TraceView traceView : traceViews)
    {
      TraceData traceData = traceView.save(selection.contains(traceView));
      traceDatas.add(traceData);
    }

    TimelineData timelineData = simulation.getTimeline().save();
    return new CircuitData(timelineData,
                           componentDatas,
                           traceDatas);
  }

  public void load(CircuitData circuitData)
  {
    clearSelection();
    simulation.getTimeline().load(circuitData.timeline);

    TraceLoader traceLoader = new TraceLoader();

    for (TraceData traceData : circuitData.traces)
    {
      traceData.create(this, traceLoader);
    }

    for (StaticData<?> staticData : circuitData.components)
    {
      staticData.createAndLoad(this, traceLoader);
    }

    validateConsistency();
  }

  public Set<PortView> connectComponentView(StaticView<?> staticView)
  {
    Set<PortView> updatedPortViews;
    if (staticView instanceof ComponentView)
    {
      updatedPortViews = connectComponentView((ComponentView<?>) staticView);
    }
    else if (staticView instanceof TunnelView)
    {
      updatedPortViews = connectTunnelView((TunnelView) staticView);
    }
    else if (staticView instanceof DecorativeView)
    {
      updatedPortViews = new LinkedHashSet<>();
    }
    else
    {
      throw new SimulatorException("Don't know how to connect component view of class [%s].", staticView.getClass().getSimpleName());
    }

    staticView.enable(simulation);
    staticView.simulationStarted(simulation);

    return updatedPortViews;
  }

  public Set<PortView> connectComponentView(ComponentView<?> componentView)
  {
    componentView.createConnections(this);

    Set<PortView> updatedPortViews = new LinkedHashSet<>();
    for (PortView portView : componentView.getPorts())
    {
      if (!updatedPortViews.contains(portView))
      {
        List<LocalConnectionNet> connectionNets = PortTraceFinder.findAndConnectTraces(simulation, portView.getConnection());
        updatedPortViews.addAll(PortTraceFinder.getPortViews(connectionNets));
      }
    }

    return updatedPortViews;
  }

  public Set<PortView> connectTunnelView(TunnelView tunnelView)
  {
    Set<PortView> updatedPortViews = new LinkedHashSet<>();
    Set<ConnectionView> updatedConnectionViews = new LinkedHashSet<>();
    for (ConnectionView connectionView : tunnelView.getConnections())
    {
      if (!updatedConnectionViews.contains(connectionView))
      {
        List<LocalConnectionNet> connectionNets = PortTraceFinder.findAndConnectTraces(simulation, connectionView);
        updatedPortViews.addAll(PortTraceFinder.getPortViews(connectionNets));
        updatedConnectionViews.addAll(PortTraceFinder.getConnectionViews(connectionNets));
      }
    }

    return updatedPortViews;
  }

  public void fireConnectionEvents(Set<PortView> updatedPortViews)
  {
    for (PortView portView : updatedPortViews)
    {
      portView.traceConnected(simulation);
    }
  }

  public Timeline getTimeline()
  {
    return simulation.getTimeline();
  }

  public Simulation getSimulation()
  {
    return simulation;
  }

  public void placeComponentView(StaticView<?> componentView)
  {
    Set<PortView> updatedPortViews = connectComponentView(componentView);

    fireConnectionEvents(updatedPortViews);
    validateConsistency();
  }

  public void startMoveComponents(List<StaticView<?>> staticViews, List<TraceView> traceViews)
  {
    Set<ConnectionView> connectionViews = new LinkedHashSet<>();
    for (StaticView<?> staticView : staticViews)
    {
      staticView.disable();
      connectionViews.addAll(disconnectView(staticView));
    }

    for (TraceView traceView : traceViews)
    {
      connectionViews.addAll(disconnectView(traceView));
    }

    connectConnections(new ArrayList<>(connectionViews));

    validateConsistency();

    clearSelection();
  }

  public void updateSelection(SelectionRectangle selectionRectangle)
  {
    selection = getSelection(selectionRectangle.getStart(), selectionRectangle.getEnd());
  }

  public void createTraceViews(List<Line> inputLines)
  {
    Set<TraceView> overlappingTraceViews = new LinkedHashSet<>();
    Set<Line> lines = new LinkedHashSet<>(inputLines);

    for (Line line : inputLines)
    {
      List<TraceOverlap> tracesOverlapping = getTracesOverlapping(line);
      for (TraceOverlap traceOverlap : tracesOverlapping)
      {
        if (traceOverlap.getOverlap() != LineOverlap.None)
        {
          TraceView traceView = traceOverlap.getTraceView();
          overlappingTraceViews.add(traceView);
          lines.add(traceView.getLine());
        }
      }
    }

    Set<Line> mergedLines = LineMinimiser.minimise(lines);
    Positions positionMap = new Positions(lines);
    LinePositionCache lineCache = new LinePositionCache(mergedLines);
    Set<Line> splitLines = LineSplitter.split(mergedLines, positionMap);
xxx
  }

  public void doneMoveComponents(List<StaticView<?>> staticViews, List<TraceView> traceViews, Set<StaticView<?>> selectedViews)
  {
    Set<PortView> updatedPortViews = new LinkedHashSet<>();

    List<Line> lines = new ArrayList<>();

    for (TraceView traceView : traceViews)
    {
      removeTraceView(traceView);
      lines.add(traceView.getLine());
    }

    List<View> selection = new ArrayList<>();

    Set<TraceView> newTraces = new LinkedHashSet<>();
    for (Line line : lines)
    {
      Set<TraceView> localNewTraces = createTraceViews(line);
      newTraces.addAll(localNewTraces);
      selection.addAll(localNewTraces);
    }

    for (StaticView<?> staticView : staticViews)
    {
      Set<PortView> portViews = connectComponentView(staticView);
      updatedPortViews.addAll(portViews);
      if (selectedViews.contains(staticView))
      {
        selection.add(staticView);
      }
    }

    newTraces = findNonRemovedTraceViews(newTraces);
    selection = findNonRemovedViews(selection);

    finaliseCreatedTraces(newTraces);

    fireConnectionEvents(updatedPortViews);
    validateConsistency();

    List<View> cleanSelection = new ArrayList<>();
    for (View view : selection)
    {
      if (view instanceof TraceView)
      {
        TraceView traceView = (TraceView) view;
        if (!traceView.hasNoConnections())
        {
          cleanSelection.add(view);
        }
      }
      else
      {
        cleanSelection.add(view);
      }
    }

    this.selection = cleanSelection;

    validateConsistency();
  }

  protected List<View> findNonRemovedViews(List<View> selection)
  {
    List<View> newSelection = new ArrayList<>();
    for (View selected : selection)
    {
      if (!(selected instanceof TraceView) || !(((TraceView) selected).hasNoConnections()))
      {
        newSelection.add(selected);
      }
    }
    return newSelection;
  }

  protected Set<TraceView> findNonRemovedTraceViews(Set<TraceView> newTraces)
  {
    Set<TraceView> correctedNewTraceViews = new LinkedHashSet<>(newTraces.size());
    for (TraceView newTrace : newTraces)
    {
      if (!newTrace.hasNoConnections())
      {
        correctedNewTraceViews.add(newTrace);
      }
    }
    return correctedNewTraceViews;
  }

  public List<View> getSelection()
  {
    return selection;
  }

  public void clearSelection()
  {
    if (!selection.isEmpty())
    {
      selection = new ArrayList<>();
    }
  }

  public boolean isSelectionEmpty()
  {
    return selection.isEmpty();
  }

  public List<View> getSelection(Float2D start, Float2D end)
  {
    boolean includeIntersections = start.x > end.x;

    Float2D boundBoxPosition = new Float2D();
    Float2D boundBoxDimension = new Float2D();
    List<View> selectedViews = new ArrayList<>();
    StaticViewIterator iterator = staticViewIterator();
    while (iterator.hasNext())
    {
      StaticView<?> staticView = iterator.next();
      updateSelectedViews(start, end, includeIntersections, boundBoxPosition, boundBoxDimension, selectedViews, staticView);
    }

    for (TraceView traceView : traceViews)
    {
      Line line = traceView.getLine();
      line.getBoundingBoxInGridSpace(boundBoxPosition, boundBoxDimension);
      if (isPoint(start, end))
      {
        if (line.isPositionOn(new Int2D(start)))
        {
          selectedViews.add(traceView);
        }
      }
      else
      {
        if (BoundingBox.containsBox(start, end, boundBoxPosition, boundBoxDimension, includeIntersections))
        {
          selectedViews.add(traceView);
        }
      }
    }
    return selectedViews;
  }

  protected void updateSelectedViews(Float2D start,
                                     Float2D end,
                                     boolean includeIntersections,
                                     Float2D boundBoxPosition,
                                     Float2D boundBoxDimension,
                                     List<View> selectedViews,
                                     StaticView<?> componentView)
  {
    if (componentView.isEnabled())
    {
      componentView.getBoundingBoxInGridSpace(boundBoxPosition, boundBoxDimension);
      if (isPoint(start, end))
      {
        if (BoundingBox.containsPoint(new Int2D(start), boundBoxPosition, boundBoxDimension))
        {
          selectedViews.add(componentView);
        }
      }
      else
      {
        if (BoundingBox.containsBox(start, end, boundBoxPosition, boundBoxDimension, includeIntersections))
        {
          selectedViews.add(componentView);
        }
      }
    }
  }

  protected boolean isPoint(Float2D start, Float2D end)
  {
    return (Math.round(start.x) == Math.round(end.x)) &&
           (Math.round(start.y) == Math.round(end.y));
  }

  public void deleteComponent(View view)
  {
    if (view != null)
    {
      if (view instanceof TraceView)
      {
        deleteTraceView((TraceView) view);
      }
      else if (view instanceof StaticView<?>)
      {
        deleteComponentView((StaticView<?>) view);
      }
      else
      {
        throw new SimulatorException("Don't know how to delete component [%s].", view.getClass().getSimpleName());
      }
    }
  }

  public void finaliseCreatedTraces(Set<TraceView> traceViews)
  {
    Set<PortView> updatedPortViews = new LinkedHashSet<>();
    int i = 0;
    for (TraceView traceView : traceViews)
    {
      if (traceView.hasNoConnections())
      {
        throw new SimulatorException("Cannot finalise a removed Trace.  Iteration [%s].", i);
      }
      Set<PortView> portViews = connectNewConnections(traceView.getStartConnection());
      updatedPortViews.addAll(portViews);
      i++;
    }

    fireConnectionEvents(updatedPortViews);
    validateConsistency();
  }

  public void deleteSelection()
  {
    for (View view : selection)
    {
      deleteComponent(view);
    }
    clearSelection();
  }

  public StaticView<?> getSingleSelectionStaticView()
  {
    StaticView<?> componentView = null;
    for (View view : selection)
    {
      if (view instanceof StaticView)
      {
        if (componentView == null)
        {
          componentView = (StaticView<?>) view;
        }
        else
        {
          return null;
        }
      }
    }
    return componentView;
  }

  public void select(View view)
  {
    selection.add(view);
  }

  public void replaceSelection(View newView, View oldView)
  {
    int i = selection.indexOf(oldView);
    if (i != -1)
    {
      selection.set(i, newView);
    }
  }

  public void addIntegratedCircuitView(IntegratedCircuitView<?, ?> integratedCircuitView)
  {
    synchronized (this)
    {
      integratedCircuitViews.add(integratedCircuitView);
    }
  }

  public void removeIntegratedCircuitView(IntegratedCircuitView<?, ?> integratedCircuitView)
  {
    synchronized (this)
    {
      integratedCircuitViews.remove(integratedCircuitView);
    }
  }

  public void addPassiveView(PassiveView<?, ?> view)
  {
    synchronized (this)
    {
      passiveViews.add(view);
    }
  }

  public void addDecorativeView(DecorativeView<?> view)
  {
    synchronized (this)
    {
      decorativeViews.add(view);
    }
  }

  protected boolean removePassiveView(PassiveView<?, ?> passiveView)
  {
    synchronized (this)
    {
      return passiveViews.remove(passiveView);
    }
  }

  protected boolean removeDecorativeView(DecorativeView<?> decorativeView)
  {
    synchronized (this)
    {
      return decorativeViews.remove(decorativeView);
    }
  }

  public void addTraceView(TraceView view)
  {
    synchronized (this)
    {
      traceViews.add(view);
    }
  }

  protected boolean removeTraceView(TraceView traceView)
  {
    if (traceView.hasNoConnections())
    {
      synchronized (this)
      {
        return traceViews.remove(traceView);
      }
    }
    else
    {
      throw new SimulatorException("Cannot remove trace view with connections.");
    }
  }

  protected boolean removeTunnelView(TunnelView tunnelView)
  {
    synchronized (this)
    {
      String name = tunnelView.getSanitisedName();
      boolean removed = this.tunnelViews.remove(tunnelView);
      if (!name.isEmpty())
      {
        Set<TunnelView> tunnelViews = this.tunnelViewsMap.get(name);
        tunnelViews.remove(tunnelView);
        if (tunnelViews.isEmpty())
        {
          this.tunnelViewsMap.remove(name);
        }
      }
      return removed;
    }
  }

  public Set<TunnelView> addTunnel(TunnelView tunnelView)
  {
    synchronized (this)
    {
      this.tunnelViews.add(tunnelView);

      String name = tunnelView.getSanitisedName();
      if (!name.isEmpty())
      {
        Set<TunnelView> tunnelViews = this.tunnelViewsMap.get(name);
        if (tunnelViews == null)
        {
          tunnelViews = new LinkedHashSet<>();
          this.tunnelViewsMap.put(name, tunnelViews);
        }
        tunnelViews.add(tunnelView);
        return tunnelViews;
      }

      return null;
    }
  }

  public Int2D getSelectionCenter()
  {
    if (isSelectionEmpty())
    {
      return null;
    }
    else
    {
      Int2D position = new Int2D();
      int count = 0;
      for (View view : selection)
      {
        if (view instanceof StaticView)
        {
          StaticView<?> staticView = (StaticView<?>) view;
          position.add(staticView.getPosition());
          count++;
        }
        else if (view instanceof TraceView)
        {
          TraceView traceView = (TraceView) view;
          position.add(traceView.getStartPosition());
          position.add(traceView.getEndPosition());
          count += 2;
        }
      }
      position.set(Math.round((float) position.x / count), Math.round((float) position.y / count));
      return position;
    }
  }

  public ConnectionView getConnection(int x, int y)
  {
    return connectionViewCache.getConnection(x, y);
  }
}

