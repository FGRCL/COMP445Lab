package networking;

public enum Method{
		GET("GET"), POST("POST");
		private String name;
	
		private Method(String name) {
			this.name = name;
		}
		
		@Override
		public String toString() {
			return name;
		}
}
