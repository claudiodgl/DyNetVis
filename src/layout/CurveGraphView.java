/* ***** BEGIN LICENSE BLOCK *****
 *
 * Copyright (c) 2014-2019 Bruno Augusto Nassif Travencolo.
 * All Rights Reserved.
 *
 * This file is part of DyNetVis Project (DyNetVis).
 *
 * How to cite this software:
 *  
@inproceedings{Linhares:2017:DSV:3019612.3019686,
 author = {Linhares, Claudio D. G. and Traven\c{c}olo, Bruno A. N. and Paiva, Jose Gustavo S. and Rocha, Luis E. C.},
 title = {DyNetVis: A System for Visualization of Dynamic Networks},
 booktitle = {Proceedings of the Symposium on Applied Computing},
 series = {SAC '17},
 year = {2017},
 isbn = {978-1-4503-4486-9},
 location = {Marrakech, Morocco},
 pages = {187--194},
 numpages = {8},
 url = {http://doi.acm.org/10.1145/3019612.3019686},
 doi = {10.1145/3019612.3019686},
 acmid = {3019686},
 publisher = {ACM},
 address = {New York, NY, USA},
 keywords = {complex networks, dynamic graph visualization, dynamic networks, recurrent neighbors, temporal activity map},
} 
 *  
 * DyNetVis is free software: you can redistribute it and/or modify it under 
 * the terms of the GNU General Public License as published by the Free 
 * Software Foundation, either version 3 of the License, or (at your option) 
 * any later version.
 *
 * DyNetVis is distributed in the hope that it will be useful, but WITHOUT 
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY 
 * or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License 
 * for more details.
 *
 * This code was developed by members of Lab of Complex Network Visualization at 
 * Federal University of Uberlândia, Brazil - (https://sites.google.com/view/dynetvis/team?authuser=0). 
 * The initial developer of the original code is Claudio D. G. Linhares <claudiodgl@gmail.com>.
 *
 * Contributor(s): Jean R. Ponciano -- jeanrobertop@gmail.com, Luis E. C. Rocha -- luis.rocha@ugent.be, 
 * José Gustavo S. Paiva -- gustavo@ufu.br, Bruno A. N. Travençolo -- travencolo@gmail.com
 *
 * You should have received a copy of the GNU General Public License along 
 * with DyNetVis. If not, see <http://www.gnu.org/licenses/>.
 *
 * ***** END LICENSE BLOCK ***** */

package layout;

import com.mxgraph.model.mxGeometry;
import com.mxgraph.util.mxPoint;
import com.mxgraph.util.mxRectangle;
import com.mxgraph.view.mxCellState;
import com.mxgraph.view.mxGraph;
import com.mxgraph.view.mxGraphView;
import java.awt.Rectangle;
import java.util.List;

class CurveGraphView extends mxGraphView {

        public CurveGraphView(mxGraph graph) {
            super(graph);
        }

        /* Only override this if you want the label to automatically position itself on the control point */
        @Override
        public mxPoint getPoint(mxCellState state, mxGeometry geometry) {
            double x = state.getCenterX();
            double y = state.getCenterY();

            if (state.getAbsolutePointCount() == 3) {
                mxPoint mid = state.getAbsolutePoint(1);
                x = mid.getX();
                y = mid.getY();
            }
            return new mxPoint(x, y);
        }
    //    /* Makes sure that the full path of the curve is included in the bounding box */ 

        @Override
        public mxRectangle updateBoundingBox(mxCellState state) {

            List<mxPoint> points = state.getAbsolutePoints();
            mxRectangle bounds = super.updateBoundingBox(state);

            Object style = state.getStyle().get("edgeStyle");
            if (CurvedEdgeStyle.KEY.equals(style) && points != null && points.size() == 3) {
                Rectangle pathBounds = CurvedShape.createPath(state.getAbsolutePoints()).getBounds();
                Rectangle union = bounds.getRectangle().union(pathBounds);
                bounds = new mxRectangle(union);
                state.setBoundingBox(bounds);
            }
            return bounds;
        }

}
