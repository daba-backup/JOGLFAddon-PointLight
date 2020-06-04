package com.github.dabasan.joglfaddon.pointlight;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.dabasan.basis.coloru8.ColorU8;
import com.github.dabasan.basis.vector.Vector;
import com.github.dabasan.joglf.gl.front.CameraFront;
import com.github.dabasan.joglf.gl.shader.ShaderProgram;

/**
 * Point light manager
 * 
 * @author Daba
 *
 */
public class PointLightMgr {
	private Logger logger = LoggerFactory.getLogger(PointLightMgr.class);
	public final int MAX_POINT_LIGHT_NUM = 256;

	private int count = 0;
	private Map<Integer, PointLight> lights_map = new HashMap<>();

	private ShaderProgram gouraud_program;
	private ShaderProgram phong_program;

	public PointLightMgr() {
		gouraud_program = new ShaderProgram("dabasan/point_light/gouraud",
				"./Data/Shader/330/addon/dabasan/point_light/gouraud/vshader.glsl",
				"./Data/Shader/330/addon/dabasan/point_light/gouraud/fshader.glsl");
		phong_program = new ShaderProgram("dabasan/point_light/phong",
				"./Data/Shader/330/addon/dabasan/point_light/phong/vshader.glsl",
				"./Data/Shader/330/addon/dabasan/point_light/phong/fshader.glsl");
		CameraFront.AddProgram(gouraud_program);
		CameraFront.AddProgram(phong_program);

		SetColorSumClamp(0.0f, 1.0f);
	}

	public void Dispose() {
		CameraFront.RemoveProgram(gouraud_program);
		CameraFront.RemoveProgram(phong_program);
	}

	public int CreatePointLight(PointLightShadingMethod method) {
		if (lights_map.size() > MAX_POINT_LIGHT_NUM) {
			logger.warn("No more point lights can be created.");
			return -1;
		}

		int light_handle = count;

		PointLight light = new PointLight();
		if (method == PointLightShadingMethod.GOURAUD) {
			light.AddProgram(gouraud_program);
		} else {
			light.AddProgram(phong_program);
		}

		lights_map.put(light_handle, light);
		count++;

		gouraud_program.Enable();
		gouraud_program.SetUniform("current_point_light_num", lights_map.size());
		gouraud_program.Disable();
		phong_program.Enable();
		phong_program.SetUniform("current_point_light_num", lights_map.size());
		phong_program.Disable();

		return light_handle;
	}
	public int DeletePointLight(int point_light_handle) {
		if (lights_map.containsKey(point_light_handle) == false) {
			logger.trace("No such point light. point_light_handle={}", point_light_handle);
			return -1;
		}

		lights_map.remove(point_light_handle);

		gouraud_program.Enable();
		gouraud_program.SetUniform("current_point_light_num", lights_map.size());
		gouraud_program.Disable();
		phong_program.Enable();
		phong_program.SetUniform("current_point_light_num", lights_map.size());
		phong_program.Disable();

		return 0;
	}
	public void DeleteAllPointLights() {
		lights_map.clear();
		count = 0;

		gouraud_program.Enable();
		gouraud_program.SetUniform("current_point_light_num", 0);
		gouraud_program.Disable();
		phong_program.Enable();
		phong_program.SetUniform("current_point_light_num", 0);
		phong_program.Disable();
	}

	public int AddProgram(int point_light_handle, ShaderProgram program) {
		if (lights_map.containsKey(point_light_handle) == false) {
			logger.trace("No such point light. point_light_handle={}", point_light_handle);
			return -1;
		}

		PointLight light = lights_map.get(point_light_handle);
		light.AddProgram(program);

		return 0;
	}
	public int RemoveAllPrograms(int point_light_handle) {
		if (lights_map.containsKey(point_light_handle) == false) {
			logger.trace("No such point light. point_light_handle={}", point_light_handle);
			return -1;
		}

		PointLight light = lights_map.get(point_light_handle);
		light.RemoveAllPrograms();

		return 0;
	}

	public int SetPosition(int point_light_handle, Vector position) {
		if (lights_map.containsKey(point_light_handle) == false) {
			logger.trace("No such point light. point_light_handle={}", point_light_handle);
			return -1;
		}

		PointLight light = lights_map.get(point_light_handle);
		light.SetPosition(position);

		return 0;
	}
	public int SetK(int point_light_handle, float k0, float k1, float k2) {
		if (lights_map.containsKey(point_light_handle) == false) {
			logger.trace("No such point light. point_light_handle={}", point_light_handle);
			return -1;
		}

		PointLight light = lights_map.get(point_light_handle);
		light.SetK(k0, k1, k2);

		return 0;
	}
	public int SetDiffuseColor(int point_light_handle, ColorU8 diffuse_color) {
		if (lights_map.containsKey(point_light_handle) == false) {
			logger.trace("No such point light. point_light_handle={}", point_light_handle);
			return -1;
		}

		PointLight light = lights_map.get(point_light_handle);
		light.SetDiffuseColor(diffuse_color);

		return 0;
	}
	public int SetDiffusePower(int point_light_handle, float diffuse_power) {
		if (lights_map.containsKey(point_light_handle) == false) {
			logger.trace("No such point light. point_light_handle={}", point_light_handle);
			return -1;
		}

		PointLight light = lights_map.get(point_light_handle);
		light.SetDiffusePower(diffuse_power);

		return 0;
	}
	public int SetColorClamp(int point_light_handle, float min, float max) {
		if (lights_map.containsKey(point_light_handle) == false) {
			logger.trace("No such point light. point_light_handle={}", point_light_handle);
			return -1;
		}

		PointLight light = lights_map.get(point_light_handle);
		light.SetColorClamp(min, max);

		return 0;
	}

	public void SetColorSumClamp(float min, float max) {
		gouraud_program.Enable();
		gouraud_program.SetUniform("point_light_color_sum_clamp_min", min);
		gouraud_program.SetUniform("point_light_color_sum_clamp_max", max);
		gouraud_program.Disable();
		phong_program.Enable();
		phong_program.SetUniform("point_light_color_sum_clamp_min", min);
		phong_program.SetUniform("point_light_color_sum_clamp_max", max);
		phong_program.Disable();
	}

	public void Update() {
		int index = 0;
		for (var light : lights_map.values()) {
			light.Update(index);
			index++;
		}
	}
}
