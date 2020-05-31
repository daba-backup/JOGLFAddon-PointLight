package com.github.dabasan.joglfaddon.pointlight;

import static com.github.dabasan.basis.coloru8.ColorU8Functions.*;

import java.util.Random;

import com.github.dabasan.joglf.gl.model.Model3DFunctions;
import com.github.dabasan.joglf.gl.shader.ShaderProgram;
import com.github.dabasan.joglf.gl.window.JOGLFWindow;

class PointLightTestWindow extends JOGLFWindow {
	private int plane_handle;

	@Override
	public void Init() {
		PointLightMgr.Initialize();

		Random random = new Random();

		int point_light_handle = PointLightMgr.CreatePointLight(ShadingMethod.PHONG);
		float r = random.nextFloat();
		float g = random.nextFloat();
		float b = random.nextFloat();
		PointLightMgr.SetDiffuseColor(point_light_handle, GetColorU8(r, g, b, 1.0f));

		plane_handle = Model3DFunctions.LoadModel("./Data/Model/OBJ/Plane/plane.obj");
		Model3DFunctions.RemoveAllPrograms(plane_handle);
		Model3DFunctions.AddProgram(plane_handle, new ShaderProgram("dabasan/point_light/phong"));
	}

	@Override
	public void Update() {
		PointLightMgr.Update();

	}

	@Override
	public void Draw() {
		Model3DFunctions.DrawModel(plane_handle);
	}
}
