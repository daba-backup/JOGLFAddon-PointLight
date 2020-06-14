package com.github.dabasan.joglfaddon.pointlight;

import static com.github.dabasan.basis.coloru8.ColorU8Functions.*;

import java.util.Random;

import com.github.dabasan.joglf.gl.model.Model3DFunctions;
import com.github.dabasan.joglf.gl.shader.ShaderProgram;
import com.github.dabasan.joglf.gl.window.JOGLFWindow;

class PointLightTestWindow extends JOGLFWindow {
	private PointLightMgr point_light_mgr;
	private int plane_handle;

	@Override
	public void Init() {
		point_light_mgr = new PointLightMgr();

		final Random random = new Random();

		final int point_light_handle = point_light_mgr
				.CreatePointLight(PointLightShadingMethod.PHONG);
		final float r = random.nextFloat();
		final float g = random.nextFloat();
		final float b = random.nextFloat();
		point_light_mgr.SetDiffuseColor(point_light_handle, GetColorU8(r, g, b, 1.0f));

		plane_handle = Model3DFunctions.LoadModel("./Data/Model/OBJ/Plane/plane.obj");
		Model3DFunctions.RemoveAllPrograms(plane_handle);
		Model3DFunctions.AddProgram(plane_handle, new ShaderProgram("dabasan/point_light/phong"));
	}

	@Override
	public void Dispose() {
		point_light_mgr.Dispose();
	}

	@Override
	public void Update() {
		point_light_mgr.Update();
	}

	@Override
	public void Draw() {
		Model3DFunctions.DrawModel(plane_handle);
	}
}
